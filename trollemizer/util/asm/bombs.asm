RandomBombTimer: {
	LDA RandomizeBombs : BEQ .disabled ; Use vanilla behavior if the randomize flag is zero
		JSL GetRandomInt
		AND #$7E
		RTL
	.disabled
		LDA VanillaBombTimer
		RTL
}