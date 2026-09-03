#!/bin/bash
sed -i 's/import ram.talia.moreiotas.api.casting.iota.ItemStackIota/import at.petrak.hexcasting.api.casting.iota.ItemIota/' Common/src/main/java/ram/talia/hexal/common/casting/arithmetics/operator/mote/OperatorMoteExtractItem.kt
sed -i 's/ItemStackIota.createFiltered/ItemIota/' Common/src/main/java/ram/talia/hexal/common/casting/arithmetics/operator/mote/OperatorMoteExtractItem.kt

sed -i 's/import ram.talia.moreiotas.api.casting.iota.ItemStackIota/import at.petrak.hexcasting.api.casting.iota.ItemIota/' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i 's/ItemStackIota.createFiltered/ItemIota/' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i 's/ItemStackIota/ItemIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt

sed -i 's/import ram.talia.moreiotas.api.casting.iota.ItemStackIota/import at.petrak.hexcasting.api.casting.iota.ItemIota/' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i 's/import ram.talia.moreiotas.api.casting.iota.ItemTypeIota/import at.petrak.hexcasting.api.casting.iota.ItemTypeIota/' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i 's/ItemStackIota/ItemIota/g' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt

sed -i 's/import ram.talia.moreiotas.api.casting.iota.ItemTypeIota/import at.petrak.hexcasting.api.casting.iota.ItemTypeIota/' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetItemTrades.kt

sed -i 's/import ram.talia.moreiotas.api.asActionResult/import at.petrak.hexcasting.api.casting.asActionResult/' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetContainedItemTypes.kt

sed -i 's/import ram.talia.moreiotas.common.casting.arithmetic.ItemArithmetic.EXTRACT_ITEM/import at.petrak.hexcasting.common.casting.arithmetic.ItemArithmetic.EXTRACT_ITEM/' Common/src/main/java/ram/talia/hexal/common/casting/arithmetics/MoteArithmetic.kt
