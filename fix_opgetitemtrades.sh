#!/bin/bash
cat << 'INNER_EOF' > Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetItemTrades.kt
package ram.talia.hexal.common.casting.actions.spells.motes

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import ram.talia.hexal.api.getVillager

object OpGetItemTrades : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val villager = args.getVillager(0, argc)

        env.caster?.let { villager.updateSpecialPrices(it) }
        villager.tradingPlayer = env.caster

        val result = villager.offers.map { offer ->
            // In 1.21 without moreiotas, replacing ItemTypeIota with something placeholder like NullIota or returning string for now
            val costList = mutableListOf(ListIota(listOf(NullIota(), DoubleIota(offer.costA.count.toDouble()))))
            if (!offer.costB.isEmpty)
                costList.add(ListIota(listOf(NullIota(), DoubleIota(offer.costB.count.toDouble()))))

            val offerList = listOf(
                    ListIota(costList as List<Iota>),
                    ListIota(listOf(NullIota(), DoubleIota(offer.result.count.toDouble())))
            )
            ListIota(offerList)
        }

        villager.stopTrading()

        return result.asActionResult
    }
}
INNER_EOF
