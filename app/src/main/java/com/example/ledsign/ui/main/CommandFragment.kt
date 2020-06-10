package com.example.ledsign.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ledsign.R


/**
 * General commands fragment
 */
class CommandFragment : Fragment(){

    private lateinit var brightnessBar: SeekBar
    private lateinit var brightnessValue: TextView
    private lateinit var scrollingTextEditable: EditText
    private lateinit var scrollingTextButton: Button
    private lateinit var staticTextEditable: EditText
    private lateinit var staticTextButton: Button

    //Initialize states
    private var stateScrollingText = false
    private var stateStaticText = false
    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
    //}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_commands, container, false)
        
        //Initialize UI objects
        brightnessValue = root.findViewById(R.id.brightnessValue)
        brightnessBar = root.findViewById(R.id.brightnessBar)
        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                val brightnessPercentage = (i/2.55).toInt()
                brightnessValue.text = "$brightnessPercentage %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Todo: Send UDP message
                //current value: seekBar.progress
            }
        })
        scrollingTextEditable = root.findViewById(R.id.scrollingEditText)
        scrollingTextButton = root.findViewById(R.id.scrollingTextButton)
        scrollingTextButton.setOnClickListener { _ ->
            when(stateScrollingText){
                false -> {
                    stateScrollingText = true
                    scrollingTextButton.text = getString(R.string.button_scrolling_text_cancel)
                    //Todo: Send UDP message
                }
                true -> {
                    stateScrollingText = false
                    scrollingTextButton.text = getString(R.string.button_scrolling_text)
                    scrollingTextEditable.setText("")
                    //Todo: Send UDP message
                }
            }
        }

        staticTextEditable = root.findViewById(R.id.staticEditText)
        staticTextButton = root.findViewById(R.id.staticTextButton)
        staticTextButton.setOnClickListener { _ ->
            when(stateStaticText){
                false -> {
                    stateStaticText = true
                    staticTextButton.text = getString(R.string.button_static_text_cancel)
                    //Todo: Send UDP message
                }
                true -> {
                    stateStaticText = false
                    staticTextButton.text = getString(R.string.button_static_text)
                    staticTextEditable.setText("")
                    //Todo: Send UDP message
                }
            }
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
            fun newInstance(sectionNumber: Int): CommandFragment {
                return CommandFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_SECTION_NUMBER, sectionNumber)
                    }
                }
            }
        }

        }