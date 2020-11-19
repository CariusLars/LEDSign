package com.example.ledsign.ui.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ledsign.ColorInterface
import com.example.ledsign.MainActivity
import com.example.ledsign.R
import com.example.ledsign.UdpClient
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException


/**
 * General commands fragment
 */
class CommandFragment : Fragment(){

    // Initialize UI elements
    private lateinit var brightnessBar: SeekBar
    private lateinit var brightnessValue: TextView
    private lateinit var scrollingTextEditable: EditText
    private lateinit var buttonScrollingText: Button
    private lateinit var staticTextEditable: EditText
    private lateinit var buttonStaticText: Button
    private lateinit var buttonScoreboardStart: Button
    private lateinit var buttonScoreboardA: Button
    private lateinit var buttonScoreboardB: Button
    private lateinit var buttonStrobo: Button
    private lateinit var buttonColorStrobo: Button
    private lateinit var buttonRainbow: Button
    private lateinit var buttonStaticColor: Button
    private lateinit var buttonReset: Button
    private lateinit var buttonSoundControl: Button

    //Initialize states
    private var stateScrollingText = false
    private var stateStaticText = false
    private var stateScoreboard = false
    private var stateStrobo = false
    private var stateColorStrobo = false
    private var stateRainbow = false
    private var stateStaticColor = false
    private var stateSoundControl = false

    private var brightness = 255

    //UDP Client
    private lateinit var client: UdpClient

    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
    //}
    /*fun sendUDPMessage(message: String){
        val buf: ByteArray = (message).toByteArray()
        val packet = DatagramPacket(buf, buf.size,MainActivity().serverAddr, MainActivity().port)
        try {MainActivity().udpSocket.send(packet)
        } catch (e: SocketException) {
            Log.e("Udp:", "Socket Error:", e);
        } catch (e: IOException) {
            Log.e("Udp Send:", "IO Error:", e);
        }
    }*/
    private var colorHandler: ColorInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ColorInterface) {
            colorHandler = context as ColorInterface
        } else {
            throw RuntimeException(
                "$context must implement YourActivityInterface"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        super.onDetach()
        colorHandler = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_commands, container, false)

        //Initialize UDP client
        client = UdpClient()


        //Initialize UI objects
        brightnessValue = root.findViewById(R.id.brightnessValue)
        brightnessBar = root.findViewById(R.id.brightnessBar)
        brightnessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                brightness = i
                val brightnessPercentage = (i/2.55).toInt()
                brightnessValue.text = "$brightnessPercentage %"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                //current value: seekBar.progress
            }
        })
        scrollingTextEditable = root.findViewById(R.id.scrollingEditText)
        buttonScrollingText = root.findViewById(R.id.scrollingTextButton)
        buttonScrollingText.setOnClickListener { _ ->
            when(stateScrollingText){
                false -> {
                    stateScrollingText = true
                    buttonScrollingText.text = getString(R.string.button_scrolling_text_cancel)

                    //Send UDP message
                    client.Message = "4:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + scrollingTextEditable.text
                    client.sendUdp()
                }
                true -> {
                    stateScrollingText = false
                    buttonScrollingText.text = getString(R.string.button_scrolling_text)
                    scrollingTextEditable.setText("")

                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + scrollingTextEditable.text
                    client.sendUdp()
                }
            }
        }

        staticTextEditable = root.findViewById(R.id.staticEditText)
        buttonStaticText = root.findViewById(R.id.staticTextButton)
        buttonStaticText.setOnClickListener { _ ->
            when(stateStaticText){
                false -> {
                    stateStaticText = true
                    buttonStaticText.text = getString(R.string.button_static_text_cancel)
                    //Send UDP message
                    client.Message = "1:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + staticTextEditable.text
                    client.sendUdp()
                }
                true -> {
                    stateStaticText = false
                    buttonStaticText.text = getString(R.string.button_static_text)
                    staticTextEditable.setText("")
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + staticTextEditable.text
                    client.sendUdp()
                }
            }
        }

        buttonScoreboardStart = root.findViewById(R.id.scoreboardStartButton)
        buttonScoreboardStart.setOnClickListener { _ ->
            when (stateScoreboard){
                false -> {
                    stateScoreboard = true
                    buttonScoreboardStart.text = getString(R.string.button_scoreboard_start_cancel)
                    //Send UDP message
                    client.Message = "7:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateScoreboard = false
                    buttonScoreboardStart.text = getString(R.string.button_scoreboard_start)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }
        buttonScoreboardA = root.findViewById(R.id.scoreboardAButton)
        buttonScoreboardA.setOnClickListener { _ ->
            //Send UDP message
            client.Message = "7:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + "A+"
            client.sendUdp()
        }
        buttonScoreboardB = root.findViewById(R.id.scoreboardBButton)
        buttonScoreboardB.setOnClickListener { _ ->
            //Send UDP message
            client.Message = "7:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + "B+"
            client.sendUdp()
        }

        buttonStrobo = root.findViewById(R.id.stroboButton)
        buttonStrobo.setOnClickListener { _ ->
            when(stateStrobo){
                false -> {
                    stateStrobo = true
                    buttonStrobo.text = getString(R.string.button_strobo_cancel)
                    //Send UDP message
                    client.Message = "6:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateStrobo = false
                    buttonStrobo.text = getString(R.string.button_strobo)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }

        buttonColorStrobo = root.findViewById(R.id.colorStroboButton)
        buttonColorStrobo.setOnClickListener { _ ->
            when(stateColorStrobo){
                false -> {
                    stateColorStrobo = true
                    buttonColorStrobo.text = getString(R.string.button_color_strobo_cancel)
                    //Send UDP message
                    client.Message = "3:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateColorStrobo = false
                    buttonColorStrobo.text = getString(R.string.button_color_strobo)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }

        buttonRainbow = root.findViewById(R.id.rainbowButton)
        buttonRainbow.setOnClickListener { _ ->
            when(stateRainbow){
                false -> {
                    stateRainbow = true
                    buttonRainbow.text = getString(R.string.button_rainbow_cancel)
                    //Send UDP message
                    client.Message = "5:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateRainbow = false
                    buttonRainbow.text = getString(R.string.button_rainbow)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }

        buttonSoundControl = root.findViewById(R.id.soundControlButton)
        buttonSoundControl.setOnClickListener{ _ ->
            when (stateSoundControl){
                false -> {
                    stateSoundControl = true
                    buttonSoundControl.text = getString(R.string.button_sound_control_cancel)
                    //Send UDP message
                    client.Message = "8:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateSoundControl = false
                    buttonSoundControl.text = getString(R.string.button_sound_control)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }

        buttonStaticColor = root.findViewById(R.id.staticColorButton)
        buttonStaticColor.setOnClickListener { _ ->
            when(stateStaticColor){
                false -> {
                    stateStaticColor = true
                    buttonStaticColor.text = getString(R.string.button_static_color_cancel)
                    //Send UDP message
                    client.Message = "2:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
                true -> {
                    stateStaticColor = false
                    buttonStaticColor.text = getString(R.string.button_static_color)
                    //Send UDP message
                    client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
                    client.sendUdp()
                }
            }
        }

        buttonReset = root.findViewById(R.id.resetButton)
        buttonReset.setOnClickListener { _ ->
            //Send UDP message
            client.Message = "0:" + Color.red(colorHandler!!.getColor()).toString() + ":" + Color.green(colorHandler!!.getColor()).toString() + ":" + Color.blue(colorHandler!!.getColor()).toString() + ":" + brightness.toString() + ":" + ""
            client.sendUdp()
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