#!/bin/bash
sed -i '/import gay.`object`/d' Common/src/main/java/ram/talia/hexal/common/entities/TickingWisp.kt
sed -i 's/return HexDebugCoreAPI.INSTANCE.getDebugEnv(caster, debugSessionId) as? WispDebugEnv/return null/g' Common/src/main/java/ram/talia/hexal/common/entities/TickingWisp.kt
sed -i '/getDebugEnv()?.let { HexDebugCoreAPI.INSTANCE.removeDebugThread(it) }/d' Common/src/main/java/ram/talia/hexal/common/entities/TickingWisp.kt

sed -i '/import gay.`object`/d' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/wisp/OpSummonWisp.kt
sed -i '/HexDebugCoreAPI.INSTANCE/,+8d' Common/src/main/java/ram/talia/hexal/common/casting/actions/spells/wisp/OpSummonWisp.kt

sed -i '/import gay.`object`/d' Common/src/main/java/ram/talia/hexal/api/casting/wisp/WispCastingManager.kt
sed -i '/if (wisp is TickingWisp && wisp.isDebugging) {/,+9d' Common/src/main/java/ram/talia/hexal/api/casting/wisp/WispCastingManager.kt

rm Common/src/main/java/ram/talia/hexal/interop/hexdebug/WispDebugEnv.kt
