package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.SpellList
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.world.inventory.TransientCraftingContainer
import net.minecraft.world.item.ItemStack
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.getItemIotaOrMoteOrList
import ram.talia.hexal.api.mulBounded
import ram.talia.hexal.api.util.Anyone

object OpCraftMotePreview : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val input = args.getItemIotaOrMoteOrList(0, OpCraftMote.argc) ?: return listOf<Iota>().asActionResult

        val griddedStacks = makeCraftingGrid(input)

        val container = TransientCraftingContainer(OpCraftMote.AutoCraftingMenu(), 3, 3)

        for ((idx, stack) in griddedStacks.withIndex()) {
            if (stack != null)
                container.setItem(idx, stack)
        }

        val (itemResult, remainingItems) = OpCraftMote.getCraftResult(container, env) ?: return emptyList<Iota>().asActionResult

        val timesToCraft = getMinCount(griddedStacks)

        val stackResult = NullIota()
        val remainingIotas = remainingItems.map { NullIota() }.toMutableList()

        remainingIotas.add(0, stackResult)
        return remainingIotas.asActionResult
    }

    private fun makeCraftingGrid(input: Anyone<EntityIota, MoteIota, SpellList>): Array<ItemStack?> {
        val out = Array<ItemStack?>(9) { _ -> null }

        for ((idy, iota) in input.flatMap({ listOf(IndexedValue(0, it)) }, { listOf(IndexedValue(0, it)) }, { it.withIndex() })) {
            when (iota) {
                is MoteIota -> out[idy * 3] = iota.record?.toStack()
                is ListIota -> {
                    for ((idx, subIota) in iota.list.withIndex()) {
                        when (subIota) {
                            is MoteIota -> out[idy * 3 + idx] = subIota.record?.toStack()
                            is NullIota -> out[idy * 3 + idx] = null
                            else -> throw MishapInvalidIota.of(input.flatMap({ it }, { it }, { ListIota(it) }), 0, "crafting_recipe")
                        }
                    }
                }
                is NullIota -> out[idy * 3] = null
                else -> throw MishapInvalidIota.of(input.flatMap({ it }, { it }, { ListIota(it) }), 0, "crafting_recipe")
            }
        }

        if (out.all { it == null })
            throw MishapInvalidIota.of(input.flatMap({ it }, { it }, { ListIota(it) }), 0, "crafting_recipe")

        return out
    }

    private fun getMinCount(griddedStacks: Array<ItemStack?>): Int = griddedStacks.minOf { iota -> iota?.count ?: Int.MAX_VALUE }
}