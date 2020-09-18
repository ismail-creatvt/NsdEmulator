package com.alohatechnology.nsdemulator.ui.server

import android.content.Context
import android.widget.ArrayAdapter
import com.alohatechnology.nsdemulator.tcp.Client

class ClientAdapter(context: Context, resource: Int, objects: MutableList<Client>) : ArrayAdapter<Client>(context, resource, objects)