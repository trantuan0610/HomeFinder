package com.tuantd.myapplication.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tuantd.myapplication.databinding.DialogSelectContactBinding

class DialogSelectContact(private val onSubmitClickListener: (Int) -> Unit) : DialogFragment()
{
    private lateinit var binding : DialogSelectContactBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogSelectContactBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.viewPhone.setOnClickListener {
            onSubmitClickListener.invoke(1)
            dismiss()
        }
        binding.viewMess.setOnClickListener{
            onSubmitClickListener.invoke(2)
            dismiss()
        }
        binding.viewMap.setOnClickListener{
            onSubmitClickListener.invoke(3)
            dismiss()
        }
        binding.viewRate.setOnClickListener{
            onSubmitClickListener.invoke(4)
            dismiss()
        }
        binding.viewReport.setOnClickListener{
            onSubmitClickListener.invoke(5)
            dismiss()
        }
        binding.viewShare.setOnClickListener {
            onSubmitClickListener.invoke(6)
            dismiss()
        }


        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.gravity = Gravity.BOTTOM

        return dialog
    }

}