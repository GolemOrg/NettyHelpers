package org.golem.netty

/**
 * The bit flag used here indicates whether
 * the var type has more bytes to be read
 */
const val VARTYPE_BIT_FLAG = 0x80
/**
 * This is the max value that a single
 * byte can hold in any of the var types
 */
const val VARTYPE_MAX_BYTE_VALUE = 0x7f
/**
 * When encoding or decoding,STEP_SIZE tells the
 * for loop how much to increment each iteration.
 */
const val VARTYPE_STEP_SIZE = 7
/**
 * These constants represent the max amount of times
 * a value type can be iterated through before being too large
 */
const val VARINT_MAX_ITERATIONS = 5
const val VARLONG_MAX_ITERATIONS = 10
/**
 * These constants represent the range of values
 * iterated over during the encoding/decoding process
 */
val VARINT_RANGE = 0..VARINT_MAX_ITERATIONS * VARTYPE_STEP_SIZE
val VARLONG_RANGE = 0..VARLONG_MAX_ITERATIONS * VARTYPE_STEP_SIZE
/**
 * These constants represent the iterators
 * used during the encoding/decoding process
 */
val VARINT_ITERATOR = VARINT_RANGE step VARTYPE_STEP_SIZE
val VARLONG_ITERATOR = VARLONG_RANGE step VARTYPE_STEP_SIZE