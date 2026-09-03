#!/bin/bash
sed -i 's/if (ResourceLocation.tryParse(typeId) == null)/if (ResourceLocation.tryParse(typeId) == null)/g' Common/src/main/java/ram/talia/hexal/api/linkable/LinkableRegistry.kt
sed -i 's/!ResourceLocation.isValidResourceLocation/ResourceLocation.tryParse(typeId) == null \/\//g' Common/src/main/java/ram/talia/hexal/api/linkable/LinkableRegistry.kt
sed -i 's/ResourceLocation.isValidResourceLocation/ResourceLocation.tryParse(typeId) != null/g' Common/src/main/java/ram/talia/hexal/api/linkable/LinkableRegistry.kt
