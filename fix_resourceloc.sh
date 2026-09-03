#!/bin/bash
sed -i 's/ResourceLocation.isValidResourceLocation/ResourceLocation.tryParse(typeId) != null \/\//g' Common/src/main/java/ram/talia/hexal/api/linkable/LinkableRegistry.kt
sed -i 's/ResourceLocation.isValidResourceLocation/ResourceLocation.tryParse(typeId) != null \/\//g' Common/src/main/java/ram/talia/hexal/api/casting/wisp/triggers/WispTriggerRegistry.kt
sed -i 's/ResourceLocation.isValidResourceLocation(s)/ResourceLocation.tryParse(s) != null/g' Common/src/main/java/ram/talia/hexal/api/config/HexalConfig.kt

sed -i 's/ResourceLocation(typeId)/ResourceLocation.parse(typeId)/g' Common/src/main/java/ram/talia/hexal/api/linkable/LinkableRegistry.kt
sed -i 's/ResourceLocation(typeId)/ResourceLocation.parse(typeId)/g' Common/src/main/java/ram/talia/hexal/api/casting/wisp/triggers/WispTriggerRegistry.kt
sed -i 's/ResourceLocation(s)/ResourceLocation.parse(s)/g' Common/src/main/java/ram/talia/hexal/api/config/HexalConfig.kt
