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
    fun registerItemCreativeTab(r: CreativeModeTab.Output, tabKey: net.minecraft.resources.ResourceKey<CreativeModeTab>) {
        for (item in ITEM_TABS.getOrDefault(tabKey, mutableListOf())) {
            r.accept(item)
        }
    }

    private val ITEMS: MutableMap<ResourceLocation, Item> = LinkedHashMap()
    private val ITEM_TABS: MutableMap<net.minecraft.resources.ResourceKey<CreativeModeTab>, MutableList<Item>> = LinkedHashMap()

    @JvmField
    val RELAY = item("relay", IXplatAbstractions.INSTANCE.getItemRelay(HexItems.props()), HexCreativeTabs.HEX_KEY)

    private fun <T : Item> item(name: String, item: T, tab: net.minecraft.resources.ResourceKey<CreativeModeTab>?): T {
        val old = ITEMS.put(HexalAPI.modLoc(name), item)
        require(old == null) { "Typo? Duplicate id $name" }
        if (tab != null) {
            ITEM_TABS.computeIfAbsent(tab) { ArrayList() }.add(item)
        }
        return item
    }
}