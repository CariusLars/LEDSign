package com.example.ledsign.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ledsign.R
import com.larswerkman.holocolorpicker.ColorPicker
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener
//import com.larswerkman.holocolorpicker.OpacityBar
import com.larswerkman.holocolorpicker.SVBar


/**
 * Color picking fragment
 */
class ColorFragment : Fragment(), OnColorChangedListener {

    private lateinit var picker: ColorPicker
    private lateinit var svBar: SVBar
    //private lateinit var opacityBar: OpacityBar
    private lateinit var button: Button
    private lateinit var text: TextView

    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
    //}

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_color, container, false)
        picker = root.findViewById(R.id.picker)
        svBar = root.findViewById(R.id.svbar)
        //opacityBar = root.findViewById(R.id.opacitybar)
        button = root.findViewById(R.id.button1)
        text = root.findViewById(R.id.textView1)

        picker.addSVBar(svBar)
        //picker.addOpacityBar(opacityBar)
        picker.onColorChangedListener = this

        button.setOnClickListener {
            text.setTextColor(picker.color)
            picker.oldCenterColor = picker.color
        }
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): ColorFragment {
            return ColorFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onColorChanged(color: Int) {
        text.text = color.toString()
    }

}