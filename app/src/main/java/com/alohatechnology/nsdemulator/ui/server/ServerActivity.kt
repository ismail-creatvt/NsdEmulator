package com.alohatechnology.nsdemulator.ui.server

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alohatechnology.nsdemulator.R
import com.alohatechnology.nsdemulator.databinding.ActivityMainBinding
import com.alohatechnology.nsdemulator.nsd.NsdHelper
import com.alohatechnology.nsdemulator.tcp.Client
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplate
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplateAdapter
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplateBottomSheetFragment
import com.alohatechnology.nsdemulator.ui.templates.ResponseTemplateBottomSheetFragment.Companion.CLASS_NAME
import com.alohatechnology.nsdemulator.util.getViewModel
import com.alohatechnology.nsdemulator.util.responseTemplates
import com.alohatechnology.nsdemulator.util.setDropdownData

class ServerActivity : AppCompatActivity(), ServerView {

    private var binding: ActivityMainBinding? = null
    private var viewModel: ServerViewModel? = null
    private var controller: ServerController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.lifecycleOwner = this

        viewModel = getViewModel(ServerViewModel::class)
        val nsdHelper = NsdHelper(this)
        //Instantiate the controller
        controller = ServerControllerImpl(nsdHelper, this, viewModel!!, this)
    }

    override fun onResume() {
        super.onResume()

        binding?.viewModel = viewModel
        binding?.controller = controller
        binding?.adapter = ResponseTemplateAdapter(responseTemplates, controller!!)
    }

    /// MCAView interface implementations:-
    override fun showToast(message: String) {
        runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    override fun refreshClientsSpinner() {
        runOnUiThread {
            if (binding?.clients?.adapter is ClientAdapter) {
                val clientsAdapter = (binding?.clients?.adapter as ClientAdapter)
                clientsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun showMessageTemplatesList() {
        ResponseTemplateBottomSheetFragment().apply {
            listener = controller!!
            show(supportFragmentManager, CLASS_NAME)
        }
    }

    override fun getResponseTemplates(): ArrayList<ResponseTemplate> {
        return responseTemplates
    }

    override fun addClient(client: Client) {
        runOnUiThread {
            viewModel?.clients?.add(client)
            if (binding?.clients?.adapter?.count != viewModel?.clients?.size) {
                setDropdownData(binding?.clients!!, viewModel?.clients)
            } else {
                (binding?.clients?.adapter as ClientAdapter?)?.notifyDataSetChanged()
            }
        }
    }

    override fun removeClient(client: Client) {
        runOnUiThread {
            if (binding?.clients?.adapter is ClientAdapter) {
                viewModel?.clients?.remove(client)
                if (binding?.clients?.adapter?.count != viewModel?.clients?.size) {
                    setDropdownData(binding?.clients!!, viewModel?.clients)
                } else {
                    (binding?.clients?.adapter as ClientAdapter?)?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun showUnregisterConfirmation() {
        AlertDialog.Builder(this)
                .setMessage("Do you want to stop this service?")
                .setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, _: Int ->
                    controller?.onUnregisterConfirm()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(R.string.no) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .show()
    }
}