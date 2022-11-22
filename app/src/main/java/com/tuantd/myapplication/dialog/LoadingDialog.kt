package com.tuantd.myapplication.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.tuantd.myapplication.R
import com.tuantd.myapplication.databinding.DialogSearchAdvBinding

open class LoadingDialog(private val onSubmitClickListener: (String) -> Unit,private val onClickListener: (String) -> Unit) : DialogFragment()  {
    private lateinit var binding : DialogSearchAdvBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSearchAdvBinding.inflate(LayoutInflater.from(context))
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btnGo.setOnClickListener {
            onSubmitClickListener.invoke(binding.edt1.text.toString())
            onClickListener.invoke(binding.edt2.text.toString())
            dismiss()
        }


        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.gravity = Gravity.BOTTOM

        return dialog
    }

}