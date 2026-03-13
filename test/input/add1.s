.include "io_arm.asm"


.data

.text
.global _start
_start:
	bl	main	 
	mov	r7,	#1	 //exit syscall
	svc	#0	 //Syscall
main :	push	{r11,r14}	 //prologue func
	mov	r0,	#3	 
	add	r0,	r0,	#10	 
	push	{r7,r14}	 //Sauvegarde r7 et lr
	mov	r7,	#4	 //Write syscall
	svc	#0	 	//syscall
	pop	{r7,r15}	 //Restauration de r7 et pc
	pop	{r11,r15}	 //epilogue func
	bx	lr
