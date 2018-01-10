package com.basicsteps.multipos.core.exceptions

class BadRequestException(val text: String) : Exception("Bad request. Cause: $text") { constructor() : this("Unknown") }