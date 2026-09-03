#!/bin/bash
sed -i '/import at.petrak.hexcasting.api.casting.iota.ItemIota/d' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i '/import at.petrak.hexcasting.api.casting.iota.ItemTypeIota/d' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i '/if (x is ItemTypeIota)/,+1d' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i '/if (x is ItemIota)/,+1d' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt
sed -i 's/ItemIota/EntityIota/g' Common/src/main/java/ram/talia/hexal/api/OperatorUtils.kt

sed -i '/import at.petrak.hexcasting.api.casting.iota.ItemIota/d' Common/src/main/java/ram/talia/hexal/common/casting/arithmetics/operator/mote/OperatorMoteExtractItem.kt
sed -i '/import at.petrak.hexcasting.api.casting.iota.ItemIota/d' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i '/import at.petrak.hexcasting.api.casting.iota.ItemTypeIota/d' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetItemTrades.kt
