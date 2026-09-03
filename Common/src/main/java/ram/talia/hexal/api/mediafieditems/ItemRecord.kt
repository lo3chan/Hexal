package ram.talia.hexal.api.mediafieditems

import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import ram.talia.hexal.api.HexalAPI
import ram.talia.hexal.api.addBounded
import ram.talia.hexal.api.config.HexalConfig
import kotlin.math.max
import kotlin.math.min

/**
 * Much of the structure of this class comes from AE2's [AEItemKey](https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/9ff272a869508125daf5727746c9d9b8b00248bd/src/main/java/appeng/api/stacks/AEItemKey.java).
 */
data class ItemRecord(var item: Item, var components: net.minecraft.core.component.DataComponentPatch?, var count: Long) {
    constructor(stack: ItemStack) : this(stack.item, stack.componentsPatch, stack.count.toLong())

    fun typeMatches(other: ItemRecord): Boolean {
        return item == other.item && components == other.components
    }

    fun typeMatches(other: ItemStack): Boolean {
        return item == other.item && components == other.componentsPatch
    }

    fun addCount(toAdd: Long) {
        count = count.addBounded(toAdd)
    }

    /**
     * Absorb the contents of another [ItemRecord] that matches this one,
     * increasing this record's count by the other's.
     */
    fun absorb(other: ItemRecord): Boolean {
        if (!typeMatches(other))
            return false

        // protection against overflow errors (really shouldn't happen but ya know why not
        val oldCount = count
        addCount(other.count)
        other.addCount(oldCount - count) // reduce the other's count by the amount moved to this' count.

        return true
    }

    fun absorb(other: ItemStack): Int {
        if (!typeMatches(other))
            return other.count

        // protection against overflow errors (really shouldn't happen but ya know why not)
        val oldCount = count
        addCount(other.count.toLong())
        return other.count - (count - oldCount).toInt()
    }

    fun split(amount: Long): ItemRecord {
        val splittee = ItemRecord(item, components, min(count, amount))
        count -= splittee.count
        return splittee
    }

    fun getDisplayName(): Component {
        val itemStack = ItemStack(item)
        components?.let { itemStack.applyComponents(it) }
        return itemStack.hoverName
    }

    fun toStack(): ItemStack = toStack(1)

    fun toStack(count: Int): ItemStack {
        if (count <= 0) {
            return ItemStack.EMPTY
        }

        val result = ItemStack(item)
        components?.let { result.applyComponents(it) }
        result.count = count
        return result
    }

    fun addDrops(amount: Long, drops: MutableList<ItemStack>) {
        var leftToTake = min(amount, count)

        while (leftToTake > 0) {
            if (drops.size > HexalConfig.server.maxItemsReturned/64) {
                HexalAPI.LOGGER.warn("Tried dropping an excessive amount of items, ignoring $leftToTake $item")
                break
            }
            val taken = min(leftToTake, item.defaultMaxStackSize.toLong())
            leftToTake -= taken
            drops.add(toStack(taken.toInt()))
        }

        // subtracts the amount taken from the remaining.
        // unless it attempted to make more than 1000 stacks,
        // this should simplify to count - amount
        // the max(..., 0) is to make sure that if the original
        // amount was greater than count it doesn't result in
        // count being less than 0.
        count = max(count - amount + leftToTake, 0)
    }

    fun writeToTag(tag: CompoundTag) {
        tag.putString(TAG_ITEM_ID, BuiltInRegistries.ITEM.getKey(this.item).toString())
        this.components?.let { tag.put(TAG_COMPONENTS, net.minecraft.core.component.DataComponentPatch.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, it).getOrThrow { IllegalStateException(it) }) }
        tag.putLong(TAG_COUNT, count)
    }

    companion object {
        const val TAG_ITEM_ID = "id"
        const val TAG_COMPONENTS = "components"
        const val TAG_COUNT = "count"

        /**
         * Taken from https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/9ff272a869508125daf5727746c9d9b8b00248bd/src/main/java/appeng/api/stacks/AEItemKey.java#L130
         */
        fun readFromTag(tag: CompoundTag): ItemRecord? {
            return try {
                val item = BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(tag.getString(TAG_ITEM_ID))).orElseThrow { IllegalArgumentException("Unknown item id.") }
                val extraTag = if (tag.contains(TAG_COMPONENTS)) net.minecraft.core.component.DataComponentPatch.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, tag.get(TAG_COMPONENTS)).getOrThrow { IllegalStateException(it) } else null
                val count = tag.getLong(TAG_COUNT)
                ItemRecord(item, extraTag, count)
            } catch (e: Exception) {
                HexalAPI.LOGGER.error("Tried to load an invalid item key from NBT: $tag, $e")
                null
            }
        }
    }
}
