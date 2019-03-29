lorom

!SWORD_USE = $7E04C3

org $0791B3
Player_HaltDashAttackLong:

org $089543
VanillaBombTimer:

org $0DBA71
GetRandomInt:

incsrc hooks.asm

org $368000
; Flag to indicate whether the bomb timers should be randomized or not
RandomizeBombs:
db $00

; Flag to indicate... something temporary
;SecretTempFlag:
;db $00

; Flag to indicate... something permanent
ActivateSuperLonk:
db $01

incsrc bombs.asm
incsrc superlonk.asm