package com.tuantd.myapplication.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tuantd.myapplication.databinding.DialogEditRoomBinding

class DialogEditRoom (private val onSubmitClickListener: (Int) -> Unit) : DialogFragment()
{
    private lateinit var binding : DialogEditRoomBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditRoomBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.viewEdit.setOnClickListener {
            onSubmitClickListener.invoke(1)
            dismiss()
        }
        binding.viewDelete.setOnClickListener{
            onSubmitClickListener.invoke(2)
            dismiss()
        }



        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.gravity = Gravity.BOTTOM

        return dialog
    }

}