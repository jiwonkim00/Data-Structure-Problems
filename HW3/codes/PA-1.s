# P1 : Digit Sum

digit_sum:
    # Initialize sum to 0
    mv    a3, a0  # Copy 'rhs' value to a3 ( lhs : a1, rhs : a3 )
    li    a0, 0
    mv    t0, a0  # Copy 'sum' to t0
    
lhs_loop:
    beqz  a1, rhs_loop     # If 'lhs' is 0, jump to rhs loop
    rem   a2, a1, 10       # a2 = 'lhs' % 10 (remainder)
    add   t0, t0, a2       # sum += a2
    div   a1, a1, 10       # 'lhs' = 'lhs' / 10
    j     lhs_loop         # Repeat the loop

rhs_loop:
    beqz  a3, done         # If 'rhs' is 0, exit the loop
    rem   a2, a3, 10       # a2 = 'rhs' % 10 (remainder)
    add   t0, t0, a2       # sum += a2
    div   a3, a3, 10       # 'rhs' = 'rhs' / 10
    j     rhs_loop         # Repeat the loop

done:
    # Move the final result from t0 to a0
    mv    a0, t0

    # Return to the caller
    jr    ra


# Digit Sum 2 (Without using pseudo instructions)
digit_sum:
    # Initialize sum to 0
    add    a3, a0, x0  # Copy 'rhs' value to a3 ( lhs : a1, rhs : a3 )
    addi   a0, x0, 0
    add    t0, a0, x0  # Copy 'sum' to t0
    addi   t1, x0, 10  # Make t1 = 10
    
lhs_loop:
    beq   a1, x0, rhs_loop     # If 'lhs' is 0, jump to rhs loop
    rem   a2, a1, t1       # a2 = 'lhs' % 10 (remainder)
    add   t0, t0, a2       # sum += a2
    div   a1, a1, t1       # 'lhs' = 'lhs' / 10
    jal   x0, lhs_loop         # Repeat the loop

rhs_loop:
    beq   a3, x0, done         # If 'rhs' is 0, exit the loop
    rem   a2, a3, t1       # a2 = 'rhs' % 10 (remainder)
    add   t0, t0, a2       # sum += a2
    div   a3, a3, t1       # 'rhs' = 'rhs' / 10
    jal   x0, rhs_loop         # Repeat the loop

done:
    # Move the final result from t0 to a0
    add    a0, t0, x0

    # Return to the caller
    jr    ra



# P2 : Fibonacci function
fibonacci:
    # Function prologue
    addi  sp, sp, -12      # Adjust the stack pointer to store 'n', 't1', and return address
    sw    ra, 8(sp)        # Save return address
    sw    a0, 0(sp)        # Save 'n' on the stack

    # Check if n <= 2
    li    t0, 2            # Load the value 2 into t0
    lw    a1, 0(sp)        # Load 'n' from the stack
    bgt   a1, t0, not_base_case # Branch if n > 2
    li    a0, 1            # Return 1 for n <= 2
    lw    ra, 8(sp)         # Restore return address
    addi  sp, sp, 12        # Restore the stack pointer (including 'n' and 't1')
    jalr  x0, 0(ra)        # Return to the caller

not_base_case:
    # Recursive case: Fibonacci(n-1)
    lw    a0, 0(sp)        # Load 'n' from the stack
    addi  a0, a0, -1       # n-1
    jal   ra, fibonacci    # Recursive call
    sw    a0, 4(sp)        # Save the result of Fibonacci(n-1) on the stack

    # Restore 'n' from the stack
    lw    a0, 0(sp)

    # Recursive case: Fibonacci(n-2)
    addi  a0, a0, -2       # n-2
    jal   ra, fibonacci    # Recursive call
    lw    a1, 4(sp)        # Load the result of Fibonacci(n-1) from the stack
    add   a0, a1, a0      # Add Fibonacci(n-1) + Fibonacci(n-2)

    # Return to the caller
    lw    ra, 8(sp)         # Restore return address
    addi  sp, sp, 12        # Restore the stack pointer (including 'n' and 't1')
    jalr  x0, 0(ra)         # Return to the caller

# P2 : Fibonacci (Without pseudo)
fibonacci:
    # Function prologue
    addi  sp, sp, -12      # Adjust the stack pointer to store 'n', 't1', and return address
    sw    ra, 8(sp)        # Save return address
    sw    a0, 0(sp)        # Save 'n' on the stack

    # Check if n <= 2
    addi  t0, x0, 2            # Load the value 2 into t0
    lw    a1, 0(sp)        # Load 'n' from the stack
    slt   t1, t0, a1        # Set t1 to 1 if t0 < a1, 0 otherwise
    bne   t1, x0, not_base_case  # Branch to not_base_case if t1 == 1
    addi  a0, x0, 1            # Return 1 for n <= 2
    lw    ra, 8(sp)         # Restore return address
    addi  sp, sp, 12        # Restore the stack pointer (including 'n' and 't1')
    jalr  x0, 0(ra)        # Return to the caller

not_base_case:
    # Recursive case: Fibonacci(n-1)
    lw    a0, 0(sp)        # Load 'n' from the stack
    addi  a0, a0, -1       # n-1
    jal   ra, fibonacci    # Recursive call
    sw    a0, 4(sp)        # Save the result of Fibonacci(n-1) on the stack

    # Restore 'n' from the stack
    lw    a0, 0(sp)

    # Recursive case: Fibonacci(n-2)
    addi  a0, a0, -2       # n-2
    jal   ra, fibonacci    # Recursive call
    lw    a1, 4(sp)        # Load the result of Fibonacci(n-1) from the stack
    add   a0, a1, a0      # Add Fibonacci(n-1) + Fibonacci(n-2)

    # Return to the caller
    lw    ra, 8(sp)         # Restore return address
    addi  sp, sp, 12        # Restore the stack pointer (including 'n' and 't1')
    jalr  x0, 0(ra)         # Return to the caller

# P3 : Tree Sum

.section .data
    .equ    VAL_OFFSET, 0
    .equ    LEFT_OFFSET, 4
    .equ    RIGHT_OFFSET, 8
    .equ    TREE_NODE_SIZE, 12  # Size of each TreeNode struct

.section .text
.globl tree

tree:

    # Initialize sum to 0
    li    t0, 0             # t0 = sum

    # Initialize head to the address of the queue (a1)
    add   t2, a1, zero     # t2 = head

    # Initialize cur to the address of the first element after the queue (a1 + TREE_NODE_SIZE)
    addi  t1, a1, TREE_NODE_SIZE  # t1 = cur

    # Copy the root node to the queue
    lw    t3, VAL_OFFSET(a0)  # Load root->val
    sw    t3, VAL_OFFSET(t2)  # Store val in queue[0]
    lw    t4, LEFT_OFFSET(a0) # Load root->left
    sw    t4, LEFT_OFFSET(t2) # Store left in queue[cur]
    lw    t5, RIGHT_OFFSET(a0) # Load root->right
    sw    t5, RIGHT_OFFSET(t2) # Store right in queue[cur]
    

loop:
    # Check if head != cur
    beq   t2, t1, done       # If head == cur, exit the loop
    
    # Load the node at head from the queue
    lw    a1, VAL_OFFSET(t2) # Load node->val
    add   t0, t0, a1        # sum += node->val

    # Load node's left child
    lw    a0, LEFT_OFFSET(t2) # a0 = pointer of left child

    # Check if left child is not NULL
    beqz  a0, skip_left      # If node->left is NULL, skip the left child
    
    lw    t3, VAL_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t3, VAL_OFFSET(t1)          # Store value of left child in queue[cur]
    lw    t4, LEFT_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t4, LEFT_OFFSET(t1)          # Store value of left child in queue[cur]
    lw    t5, RIGHT_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t5, RIGHT_OFFSET(t1)          # Store value of left child in queue[cur]
    addi  t1, t1, TREE_NODE_SIZE  # Increment cur by TREE_NODE_SIZE

skip_left:
    # Load node's right child
    lw    a0, RIGHT_OFFSET(t2) # a0 = pointer of right child

    # Check if right child is not NULL
    beqz  a0, skip_right     # If node->right is NULL, skip the right child
    lw    t3, VAL_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t3, VAL_OFFSET(t1)          # Store value of left child in queue[cur]
    lw    t4, LEFT_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t4, LEFT_OFFSET(t1)          # Store value of left child in queue[cur]
    lw    t5, RIGHT_OFFSET(a0)          # t4 : Get value of the left child (a0: pointer of left child)
    sw    t5, RIGHT_OFFSET(t1)          # Store value of left child in queue[cur]
    addi  t1, t1, TREE_NODE_SIZE  # Increment cur by TREE_NODE_SIZE

skip_right:
    addi  t2, t2, TREE_NODE_SIZE  # Increment head by TREE_NODE_SIZE
    j     loop               # Repeat the loop

done:
    # Move the final result from t0 to a0
    mv    a0, t0

    jr ra
