package ram.talia.hexal.common.lib

import at.petrak.hexcasting.common.lib.HexCreativeTabs
import at.petrak.hexcasting.common.lib.HexItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import ram.talia.hexal.api.HexalAPI
import ram.talia.hexal.xplat.IXplatAbstractions
import java.util.function.BiConsumer


object HexalItems {
    @JvmStatic
    fun registerItems(r: BiConsumer<Item, ResourceLocation>) {
        for ((key, value) in ITEMS) {
            r.accept(value, key)
        }
    }

    @JvmStatic
    fun registerItemCreativeTab(r: CreativeModeTab.Output, tab: CreativeModeTab) {
        for (item in ITEM_TABS.getOrDefault(tab, mutableListOf())) {
            r.accept(item)
        }
    }

    private val ITEMS: MutableMap<ResourceLocation, Item> = LinkedHashMap()
    private val ITEM_TABS: MutableMap<CreativeModeTab, MutableList<Item>> = LinkedHashMap()

    @JvmField
    val RELAY = item("relay", IXplatAbstractions.INSTANCE.getItemRelay(HexItems.props()), HexCreativeTabs.HEX)

    private fun <T : Item> item(name: String, item: T, tab: CreativeModeTab?): T {
        val old = ITEMS.put(HexalAPI.modLoc(name), item)
        require(old == null) { "Typo? Duplicate id $name" }
        if (tab != null) {
            ITEM_TABS.computeIfAbsent(tab) { t -> ArrayList() }.add(item)
        }
        return item
    }
}