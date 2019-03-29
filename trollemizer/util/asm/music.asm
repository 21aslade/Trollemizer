OverwriteSongBanks: {
    LDA OnlyLick : BEQ HookEnd ; If lick mode is 0, don't reset the music
        ; Set the pointer to the song banks
        db #$A9, Lick>>16 ; LDA.b high_byte
        STA $00
        db #$A9, Lick>>8 ; LDA.b mid_byte
        STA $01
        db #$A9, Lick; LDA.b low_byte
        STA $02
    HookEnd:
    ; Code that was overwritten for the hook
    REP #$30
    LDY.w #$0000
    RTL
}