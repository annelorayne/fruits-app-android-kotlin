package br.com.alm.tropicalfruits.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.alm.tropicalfruits.R
import br.com.alm.tropicalfruits.models.Fruit


class FruitAndVegAdapter(private val context: Context,
                         private val list: List<Fruit>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var dataSource = list

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setDataSource(list: List<Fruit>) {
        this.dataSource = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_fruit_and_veg, parent, false)
            holder =
                ViewHolder()
            holder.titleTextView = view.findViewById(R.id.fruit_ang_veg_list_name) as TextView
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }
        val titleTextView = holder.titleTextView

        val fruit = getItem(position) as Fruit
        titleTextView.text = fruit.name
        return view
    }

    private class ViewHolder {
        lateinit var titleTextView: TextView
    }
}
