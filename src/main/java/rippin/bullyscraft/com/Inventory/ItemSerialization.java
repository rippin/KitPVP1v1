package rippin.bullyscraft.com.Inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.server.v1_7_R3.NBTBase;
import net.minecraft.server.v1_7_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R3.NBTReadLimiter;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

// For 1.7.9
public class ItemSerialization {
    // Current serialization version - we will use this to handle compatiblity
    private static final int VERSION = 1;

    // Types we can serialize
    private static final String INVENTORY_CUSTOM = "custom";
    private static final String INVENTORY_PLAYER = "player";

    // NBT types
    private static final int NBT_TYPE_COMPOUND = 10;

    // NBT fields
    private static final String NBT_TYPE = "type";
    private static final String NBT_ITEMS = "items";
    private static final String NBT_VERSION = "version";
    private static final String NBT_PLAYER_HELD_INDEX = "player_held_index";
    private static final String NBT_PLAYER_NAME = "player_name"; // TODO: Change to UUID

    private static Method WRITE_NBT;
    private static Method READ_NBT;

    public static String toBase64(Inventory inventory) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        NBTTagCompound root = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();

        String type = INVENTORY_CUSTOM;
        int size = inventory.getSize();

        // Inventory types we support
        if (inventory instanceof PlayerInventory) {
            PlayerInventory playerInventory = (PlayerInventory) inventory;
            root.setInt(NBT_PLAYER_HELD_INDEX, playerInventory.getHeldItemSlot());
            root.setString(NBT_PLAYER_NAME, playerInventory.getHolder().getName());
            type = INVENTORY_PLAYER;
            size += 4;
        }
        // Save every element in the list
        for (int i = 0; i < size; i++) {
            NBTTagCompound outputObject = new NBTTagCompound();
            CraftItemStack craft = getCraftVersion(inventory.getItem(i));
            net.minecraft.server.v1_7_R3.ItemStack nmsCopy = CraftItemStack.asNMSCopy(craft);

            // Convert the item stack to a NBT compound
            if (nmsCopy != null) {
                nmsCopy.save(outputObject);
            }
            itemList.add(outputObject);
        }

        // Now save the list
        root.setString(NBT_TYPE, type);
        root.setInt(NBT_VERSION, VERSION);
        root.set(NBT_ITEMS, itemList);
        writeNbt(root, dataOutput);

        // Serialize that array
        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    public static Inventory fromBase64(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        NBTTagCompound root = (NBTTagCompound) readNbt(new DataInputStream(inputStream), 0); new NBTTagCompound();

        // Future versions might handle older versions instead
        if (root.getInt(NBT_VERSION) != VERSION) {
            throw new IllegalArgumentException("Incompatible version: " + root.getInt(NBT_VERSION));
        }
        NBTTagList itemList = root.getList(NBT_ITEMS, NBT_TYPE_COMPOUND);
        String type = root.getString(NBT_TYPE);
        return parseInventory(root, itemList, type);
    }

    private static Inventory parseInventory(NBTTagCompound root, NBTTagList itemList, String type) {
        Inventory inventory = new CraftInventoryCustom(null, itemList.size());

        for (int i = 0; i < itemList.size(); i++) {
            NBTTagCompound inputObject = (NBTTagCompound) itemList.get(i);

            if (!inputObject.isEmpty()) {
                inventory.setItem(i, CraftItemStack.asCraftMirror(
                        net.minecraft.server.v1_7_R3.ItemStack.createStack(inputObject)));
            }
        }
        // Handle different types
        if (INVENTORY_CUSTOM.equals(type)) {
            return inventory;
        } else if (INVENTORY_PLAYER.equals(type)) {
            return new OfflinePlayerInventory(inventory,
                    root.getInt(NBT_PLAYER_HELD_INDEX),
                    root.getString(NBT_PLAYER_NAME));
        } else {
            throw new IllegalArgumentException("Unexpected inventory type: " + type);
        }
    }

    private static void writeNbt(NBTBase base, DataOutput output) {
        if (WRITE_NBT == null) {
            try {
                WRITE_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", NBTBase.class, DataOutput.class);
                WRITE_NBT.setAccessible(true);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to find private write method.", e);
            }
        }

        try {
            WRITE_NBT.invoke(null, base, output);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to write " + base + " to " + output, e);
        }
    }

    private static NBTBase readNbt(DataInput input, int level) {
        if (READ_NBT == null) {
            try {
                // NBTReadLimiter is new in 1.7.9
                READ_NBT = NBTCompressedStreamTools.class.getDeclaredMethod("a", DataInput.class, int.class, NBTReadLimiter.class);
                READ_NBT.setAccessible(true);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to find private read method.", e);
            }
        }

        try {
            return (NBTBase) READ_NBT.invoke(null, input, level, NBTReadLimiter.a);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to read from " + input, e);
        }
    }

    public static Inventory getInventoryFromArray(ItemStack[] items) {
        CraftInventoryCustom custom = new CraftInventoryCustom(null, items.length);

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                custom.setItem(i, items[i]);
            }
        }
        return custom;
    }

    private static CraftItemStack getCraftVersion(ItemStack stack) {
        if (stack instanceof CraftItemStack)
            return (CraftItemStack) stack;
        else if (stack != null)
            return CraftItemStack.asCraftCopy(stack);
        else
            return null;
    }

    private static class OfflinePlayerInventory extends ForwardingInventory implements PlayerInventory {
        private int heldItemSlot;
        private String playerName;

        public OfflinePlayerInventory(Inventory delegate, int heldItemSlot, String playerName) {
            super(delegate, InventoryType.PLAYER);
            this.heldItemSlot = heldItemSlot;
        }

        @SuppressWarnings("deprecation")
        @Override
        public HumanEntity getHolder() {
            return Bukkit.getPlayer(playerName);
        }

        @Override
        public int getSize() {
            // Offset
            return super.getSize() - 4;
        }

        @Override
        public ItemStack[] getContents() {
            return Arrays.copyOf(super.getContents(), getSize());
        }

        @Override
        public ItemStack[] getArmorContents() {
            return Iterables.toArray(Iterables.skip(this, getSize()), ItemStack.class);
        }

        @Override
        public ItemStack getHelmet() {
            return getItem(getSize() + 3);
        }

        @Override
        public ItemStack getChestplate() {
            return getItem(getSize() + 2);
        }

        @Override
        public ItemStack getLeggings() {
            return getItem(getSize() + 1);
        }

        @Override
        public ItemStack getBoots() {
            return getItem(getSize() + 0);
        }

        @Override
        public void setArmorContents(ItemStack[] items) {
            int cnt = getSize();

            if (items == null) {
                items = new org.bukkit.inventory.ItemStack[4];
            }
            for (org.bukkit.inventory.ItemStack item : items) {
                setItem(cnt++, item);
            }
        }

        @Override
        public void setHelmet(ItemStack helmet) {
            setItem(getSize() + 3, helmet);
        }

        @Override
        public void setChestplate(ItemStack chestplate) {
            setItem(getSize() + 2, chestplate);
        }

        @Override
        public void setLeggings(ItemStack leggings) {
            setItem(getSize() + 1, leggings);
        }

        @Override
        public void setBoots(ItemStack boots) {
            setItem(getSize() + 0, boots);
        }

        @Override
        public ItemStack getItemInHand() {
            return getItem(heldItemSlot);
        }

        @Override
        public void setItemInHand(ItemStack stack) {
            setItem(getHeldItemSlot(), stack);
        }

        @Override
        public int getHeldItemSlot() {
            return heldItemSlot;
        }

        @Override
        public void setHeldItemSlot(int slot) {
            Preconditions.checkArgument((slot >= 0) && (slot < 8), "Slot is not between 0 and 8 inclusive");
            this.heldItemSlot = slot;
        }

        @Override
        @Deprecated
        public int clear(int paramInt1, int paramInt2) {
            throw new UnsupportedOperationException();
        }
    }

    private static class ForwardingInventory implements Inventory {
        protected Inventory delegate;
        protected InventoryType type;

        /**
         * Construct a new inventory that forwards all method calls to another inventory.
         * @param delegate - the inventory delegate.
         * @param type - the type.
         */
        public ForwardingInventory(Inventory delegate, InventoryType type) {
            this.delegate = delegate;
            this.type = type;
        }

        @Override
        public InventoryType getType() {
            if (type != null)
                return type;
            return delegate.getType();
        }

        @Override
        public int getSize() {
            return delegate.getSize();
        }

        @Override
        public int getMaxStackSize() {
            return delegate.getMaxStackSize();
        }

        @Override
        public void setMaxStackSize(int paramInt) {
            delegate.setMaxStackSize(paramInt);
        }

        @Override
        public String getName() {
            return delegate.getName();
        }

        @Override
        public ItemStack getItem(int paramInt) {
            return delegate.getItem(paramInt);
        }

        @Override
        public void setItem(int paramInt, ItemStack paramItemStack) {
            delegate.setItem(paramInt, paramItemStack);
        }

        @Override
        public HashMap<Integer, ItemStack> addItem(ItemStack... paramArrayOfItemStack)
                throws IllegalArgumentException {
            return delegate.addItem(paramArrayOfItemStack);
        }

        @Override
        public HashMap<Integer, ItemStack> removeItem(ItemStack... paramArrayOfItemStack)
                throws IllegalArgumentException {
            return delegate.removeItem(paramArrayOfItemStack);
        }

        @Override
        public ItemStack[] getContents() {
            return delegate.getContents();
        }

        @Override
        public void setContents(ItemStack[] paramArrayOfItemStack) throws IllegalArgumentException {
            delegate.setContents(paramArrayOfItemStack);
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean contains(int paramInt) {
            return delegate.contains(paramInt);
        }

        @Override
        public boolean contains(Material paramMaterial) throws IllegalArgumentException {
            return delegate.contains(paramMaterial);
        }

        @Override
        public boolean contains(ItemStack paramItemStack) {
            return delegate.contains(paramItemStack);
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean contains(int paramInt1, int paramInt2) {
            return delegate.contains(paramInt1, paramInt2);
        }

        @Override
        public boolean contains(Material paramMaterial, int paramInt)
                throws IllegalArgumentException {
            return delegate.contains(paramMaterial, paramInt);
        }

        @Override
        public boolean contains(ItemStack paramItemStack, int paramInt) {
            return delegate.contains(paramItemStack, paramInt);
        }

        @Override
        public boolean containsAtLeast(ItemStack paramItemStack, int paramInt) {
            return delegate.containsAtLeast(paramItemStack, paramInt);
        }

        @Override
        @SuppressWarnings("deprecation")
        public HashMap<Integer, ? extends ItemStack> all(int paramInt) {
            return delegate.all(paramInt);
        }

        @Override
        public HashMap<Integer, ? extends ItemStack> all(Material paramMaterial)
                throws IllegalArgumentException {
            return delegate.all(paramMaterial);
        }

        @Override
        public HashMap<Integer, ? extends ItemStack> all(ItemStack paramItemStack) {
            return delegate.all(paramItemStack);
        }

        @Override
        @SuppressWarnings("deprecation")
        public int first(int paramInt) {
            return delegate.first(paramInt);
        }

        @Override
        public int first(Material paramMaterial) throws IllegalArgumentException {
            return delegate.first(paramMaterial);
        }

        @Override
        public int first(ItemStack paramItemStack) {
            return delegate.first(paramItemStack);
        }

        @Override
        public int firstEmpty() {
            return delegate.firstEmpty();
        }

        @Override
        @SuppressWarnings("deprecation")
        public void remove(int paramInt) {
            delegate.remove(paramInt);
        }

        @Override
        public void remove(Material paramMaterial) throws IllegalArgumentException {
            delegate.remove(paramMaterial);
        }

        @Override
        public void remove(ItemStack paramItemStack) {
            delegate.remove(paramItemStack);
        }

        @Override
        public void clear(int paramInt) {
            delegate.clear(paramInt);
        }

        @Override
        public void clear() {
            delegate.clear();
        }

        @Override
        public List<HumanEntity> getViewers() {
            return delegate.getViewers();
        }

        @Override
        public String getTitle() {
            return delegate.getTitle();
        }

        @Override
        public InventoryHolder getHolder() {
            return delegate.getHolder();
        }

        @Override
        public ListIterator<ItemStack> iterator() {
            return delegate.iterator();
        }

        @Override
        public ListIterator<ItemStack> iterator(int paramInt) {
            return delegate.iterator(paramInt);
        }

        @Override
        public String toString() {
            return type.toString();
        }
    }
}