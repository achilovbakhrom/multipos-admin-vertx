package com.basicsteps.multipos.core

class DbException(var code: Int, message: String) : Exception() {
    companion object {
        val SAVED_CODE = 0
        val DB_FAILED_CODE = 1
        val SUCH_DATA_EXISTS_CODE = 2
        val DELETE_CODE = 3
        val UPDATE_CODE = 4
        val SUCH_DATA_NOT_EXISTS_CODE = 5
        val WRONG_ACCESS_CODE = 6

        val SAVED_MESSAGE = "Saved successful!!!"
        val DB_FAILED_MESSAGE = "Db operation is failed!!!"
        val SUCH_DATA_EXISTS_MESSAGE = "Such data is already exists!!!"
        val DELETE_MESSAGE = "Deleted successful!!!"
        val UPDATE_MESSAGE = "Updated successful"
        val SUCH_DATA_NOT_EXISTS_MESSAGE = "Such data is not exists!!!"
        val WRONG_ACCESS_MESSAGE = "Access code does not match!!!"
    }
}