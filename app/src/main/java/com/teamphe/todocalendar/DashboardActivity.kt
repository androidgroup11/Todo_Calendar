package com.teamphe.todocalendar

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : Fragment() {

    lateinit var dbHandler: DBHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dbHandler = DBHandler(this.context!!)
        rv_dashboard.layoutManager = LinearLayoutManager(this.context)

        //Bấm vào nút fab trên activity_dashboard.xml thì thêm vào một công việc mới
        fab_dashboard.setOnClickListener {
            val dialog = AlertDialog.Builder(this.context!!)    //Tạo 1 dialog mới
            dialog.setTitle("Add ToDo")
            val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
            val toDoName = view.findViewById<EditText>(R.id.ev_todo)

            //Đổi sang view dialog_dashboard.xml
            dialog.setView(view)

            //Option Add để thêm một công việc mới và lưu vào database
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                if (toDoName.text.isNotEmpty()) {
                    val toDo = ToDo()
                    toDo.name = toDoName.text.toString()
                    dbHandler.addToDo(toDo)
                    refreshList()
                }
            }

            //Option Cancel
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            }
            dialog.show()
        }

    }

    //Phương thức để đổi tên công việc và lưu tên mới vào database
    fun updateToDo(toDo: ToDo){
        val dialog = AlertDialog.Builder(this.context!!)
        dialog.setTitle("Update ToDo")
        val view = layoutInflater.inflate(R.layout.dialog_dashboard, null)
        val toDoName = view.findViewById<EditText>(R.id.ev_todo)
        toDoName.setText(toDo.name)
        dialog.setView(view)
        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (toDoName.text.isNotEmpty()) {
                toDo.name = toDoName.text.toString()
                dbHandler.updateToDo(toDo)
                refreshList()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()
    }

    //Khi mở lại ứng dụng thì sẽ thấy được danh sách trước đó
    override fun onResume() {
        refreshList()
        super.onResume()
    }

    //Phương thức để refresh lại danh sách sau khi đã thêm vào 1 công việc mới
    private fun refreshList(){
        rv_dashboard.adapter = DashboardAdapter(this,dbHandler.getToDos())
    }

    //Tạo adapter để liên kết dữ liệu với view
    //Để sử dụng các thuộc tính như refreshList() thì cần 1 đối tượng DashboardActivity
    class DashboardAdapter(val activity: DashboardActivity, val list: MutableList<ToDo>) :
        RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

        //Tạo ViewHolder mới để tối ưu listview
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val toDoName: TextView = v.findViewById(R.id.tv_todo_name)
            val menu : ImageView = v.findViewById(R.id.iv_menu)
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity.context).inflate(R.layout.rv_child_dashboard, p0, false))
        }

        //Trả về kích thước của RecyclerView
        override fun getItemCount(): Int {
            return list.size
        }

        //Phương thức để gắn data và view
        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.toDoName.text = list[p1].name

            //Khi bấm vào tên công việc thì sẽ chuyển sang màn hình ItemActivity, truyền id và name của công việc đó
            holder.toDoName.setOnClickListener {
                val intent = Intent(activity.context,ItemActivity::class.java)
                intent.putExtra(INTENT_TODO_ID,list[p1].id)
                intent.putExtra(INTENT_TODO_NAME,list[p1].name)
                activity.startActivity(intent)
            }

            //Xử lí sự kiện khi bấm vào menu
            holder.menu.setOnClickListener {
                val popup = PopupMenu(activity.context,holder.menu)
                popup.inflate(R.menu.dashboard_child)

                popup.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menu_edit->{
                            activity.updateToDo(list[p1])
                        }
                        R.id.menu_delete->{
                            val dialog = AlertDialog.Builder(activity.context!!)
                            dialog.setTitle("Are you sure")
                            dialog.setMessage("Do you want to delete this task ?")
                            dialog.setPositiveButton("Continue") { _: DialogInterface, _: Int ->
                                activity.dbHandler.deleteToDo(list[p1].id)
                                activity.refreshList()
                            }
                            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

                            }
                            dialog.show()
                        }
                        R.id.menu_mark_as_completed->{
                            activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,true)
                        }
                        R.id.menu_reset->{
                            activity.dbHandler.updateToDoItemCompletedStatus(list[p1].id,false)
                        }
                    }

                    true
                }
                popup.show()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_dashboard, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}