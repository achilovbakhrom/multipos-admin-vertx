package com.basicsteps.multipos.core.exceptions

class NotExistsException(column: String, value: String) : Exception("$value not found in database!!!")