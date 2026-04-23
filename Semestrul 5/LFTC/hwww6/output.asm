extern printf
extern scanf
section .data
	format_in db "%d", 0
	format_out db "%d", 10, 0
	a dd 0
	b dd 0
	res dd 0

section .text
	global main
main:
	push rbp
	mov rbp, rsp
	mov rsi, a
	mov rdi, format_in
	xor rax, rax
	call scanf
	mov rsi, b
	mov rdi, format_in
	xor rax, rax
	call scanf
	mov eax, [a]
	push rax
	mov eax, [b]
	push rax
	pop rbx
	pop rax
	add rax, rbx
	push rax
	mov eax, [b]
	push rax
	mov eax, [a]
	push rax
	pop rbx
	pop rax
	imul rax, rbx
	push rax
	pop rbx
	pop rax
	sub rax, rbx
	push rax
	mov rax, 7
	push rax
	pop rbx
	pop rax
	add rax, rbx
	push rax
	pop rax
	mov [res], eax
	mov eax, [res]
	push rax
	pop rsi
	mov rdi, format_out
	xor rax, rax
	call printf
	mov rsp, rbp
	pop rbp
	ret
