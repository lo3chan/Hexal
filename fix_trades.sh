#!/bin/bash
sed -i 's/ItemTypeIota(offer/at.petrak.hexcasting.api.casting.iota.EntityIota(net.minecraft.world.entity.item.ItemEntity(net.minecraft.world.entity.EntityType.ITEM, null))/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetItemTrades.kt
sed -i 's/ItemTypeIota/at.petrak.hexcasting.api.casting.iota.EntityIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpGetItemTrades.kt
