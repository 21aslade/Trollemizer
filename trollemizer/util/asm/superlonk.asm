ToggleSuperLonk: {
    LDA ActivateSuperLonk : BEQ .disabled ; Use vanilla behavior if the flag is zero
    LDA !SWORD_USE : EOR #$01 : STA !SWORD_USE ; Toggle sword use Flag
.disabled
    LDA $7EF340 ; Code that was overwritten by the hook 
    RTL
}

SwordSwingBlock: {
    LDA ActivateSuperLonk : BEQ .disabled ; Use vanilla behavior if the flag is zero
    LDA !SWORD_USE : AND #$01 : BEQ .noSwing ; If the sword use flag is unset, don't swing sword
.disabled
    LDA $0301 : ORA $037A ; Code that was overwritten by the hook
    RTL

.noSwing
    LDA #$01 ; Set A to nonzero to trigger the branch to the end of the Player_Sword routine
    RTL
}

DashBlock: {
    LDA ActivateSuperLonk : BEQ .disabled ; Use vanilla behavior if the flag is zero
    LDA !SWORD_USE : AND #$01 : BEQ .nodash ; If the sword use flag is unset, don't allow dash
.disabled
    LDA $00 : STA $06 ; Code overwritten by the hook
    RTL
.nodash
    JSL Player_HaltDashAttackLong
    LDA $00 : STA $06
    RTL
}

ItemBlock: {
    LDA ActivateSuperLonk : BEQ .disabled ; Use vanilla behavior if the flag is zero
    LDA !SWORD_USE : AND #$01 : BNE .noitem ; If the sword use flag is set, don't use item.
.disabled
    LDA $3C : BEQ .zero ; If it's zero, BCC triggers, which we don't want
    RTL
.zero
    LDA #$9 ; Make A equal to 9 so it doesn't block
    RTL
.noitem
    LDA #$00 ; Make A less than 9 so it blocks the item - assumes you're using sword, which is fitting
    RTL
}