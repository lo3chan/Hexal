#!/bin/bash
sed -i 's/ItemIota(/EntityIota(null)/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i 's/is ItemIota/is EntityIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i 's/Anyone<ItemIota/Anyone<EntityIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
sed -i 's/Anyone<EntityIota/Anyone<EntityIota/g' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/motes/OpCraftMotePreview.kt
