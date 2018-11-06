package com.johnhigginsmavila.rcrtskotlinapp.Adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.johnhigginsmavila.rcrtskotlinapp.Controller.HostListActivity
import com.johnhigginsmavila.rcrtskotlinapp.Model.Host
import com.johnhigginsmavila.rcrtskotlinapp.R
import java.util.*

class HostListAdapter (val context: Context, val hosts: ArrayList<Host>, val hostClick: (Host) -> Unit) : RecyclerView.Adapter<HostListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HostListAdapter.Holder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.host_list_item, parent, false)
        return Holder(view, hostClick)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return hosts.count()
    }

    override fun onBindViewHolder(holder: HostListAdapter.Holder, position: Int) {
        holder?.bindCategory(hosts[position], context)
    }

    inner class Holder(itemView: View, val itemClick: (Host) -> Unit) : RecyclerView.ViewHolder(itemView) {

        val name = itemView?.findViewById<TextView>(R.id.hostListNameTxt)
        val email = itemView?.findViewById<TextView>(R.id.hostListEmailTxt)
        val hostNature = itemView?.findViewById<TextView>(R.id.hostListNatureTxt)
        val location = itemView?.findViewById<TextView>(R.id.hostListLocationTxt)
        val description = itemView?.findViewById<TextView>(R.id.hostListDescTxt)
        val hostImage = itemView?.findViewById<ImageView>(R.id.hostListImg)
        val sendRequest = itemView?.findViewById<Button>(R.id.hostListBtn)


        fun bindCategory (host: Host, context: Context) {

            name?.text = host.name
            email?.text = host.email
            hostNature?.text = host.hostNature
            location?.text = host.location
            description?.text = host.description

            setImage()

            sendRequest.setOnClickListener {
                itemClick(host)
            }
        }

        fun setImage () {
            try {
                var userAvatar = "profileDefault"
                var avatarColor = "[0.5, 0.5, 0.5, 1]"
                val random = Random()
                val color = random.nextInt(2)
                val avatar = random.nextInt(28)

                if (color == 0) {
                    userAvatar = "light$avatar"
                } else {
                    userAvatar = "dark$avatar"
                }

                val r = random.nextInt(255)
                val g = random.nextInt(255)
                val b = random.nextInt(255)


                val resourceId = context.resources.getIdentifier(userAvatar, "drawable", context.packageName)

                hostImage?.setImageResource(resourceId)
                hostImage?.setBackgroundColor(Color.rgb(r,g,b))
            }
            catch (e: Exception) {
                Log.d("SET_IMAGE_ERROR", e.localizedMessage)
            }
        }
    }
}