package ua.shishkoam.appcoockingbook.utils.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.show_function_dialog.*
import ua.shishkoam.appcoockingbook.R

class ShowFunctionInfoDialog : DialogFragment() {

    companion object {
        private const val MESSAGE = "message"
        private const val ALWAYS_SHOW = "alwaysShow"
        private const val PREF_NOT_SHOW_STRING = "prefNotShowString"


        fun newInstance(message: String) = ShowFunctionInfoDialog().apply {
            arguments = Bundle(2).apply {
                putString(MESSAGE, message)
                putBoolean(ALWAYS_SHOW, true)
            }
        }

        fun newInstance(message: String, prefNotShowString: String) =
            ShowFunctionInfoDialog().apply {
                arguments = Bundle(3).apply {
                    putString(MESSAGE, message)
                    putBoolean(ALWAYS_SHOW, false)
                    putString(PREF_NOT_SHOW_STRING, prefNotShowString)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.show_function_dialog, container)
        isCancelable = true
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        information.text = arguments?.getString(MESSAGE)
        if (arguments?.getBoolean(ALWAYS_SHOW) == true) {
            btnNotShow.visibility = GONE
        } else {
            val prefString = arguments?.getString(PREF_NOT_SHOW_STRING)
            if (prefString != null && prefString.isNotEmpty()) {
                btnNotShow.setOnClickListener {
//                    PreferencesManager.setValue(activity, prefString, true)
                    dismiss()
                }
            }
        }

        btnOk.setOnClickListener {
            dismiss()
        }
        dialog?.setTitle(getString(R.string.information))
        dialog?.setCanceledOnTouchOutside(true)
    }
}