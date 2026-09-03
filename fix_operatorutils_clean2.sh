#!/bin/bash
sed -i 's/ItemIota(stack)/EntityIota(net.minecraft.world.entity.item.ItemEntity(net.minecraft.world.entity.EntityType.ITEM, null))/g' Common/src/main/java/ram/talia/hexal/common/casting/arithmetics/operator/mote/OperatorMoteExtractItem.kt
sed -i 's/EntityIota/EntityIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
