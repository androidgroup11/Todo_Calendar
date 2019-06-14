package com.teamphe.todocalendar

//Tạo 1 lớp với các thuộc tính để lưu thông tin của ToDoList
class ToDo {

    var id: Long = -1
    var name = ""
    var createdAt = ""
    var items: MutableList<ToDoItem> = ArrayList()  //Các Item trong list có thể chỉnh sửa được nên thuộc kiểu MutableList

}