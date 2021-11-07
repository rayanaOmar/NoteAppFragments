package com.example.noteappfragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController


class fragment_update : Fragment() {

    private lateinit var etNote: EditText
    private lateinit var btUpdate: Button

    lateinit var updateViewModel: UpdateViewModel
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        //To access MainActivity here
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        //Update view model
        updateViewModel = ViewModelProvider(this).get(UpdateViewModel::class.java)

        //initialization
        etNote = view.findViewById(R.id.etNoteUpdate)
        btUpdate = view.findViewById(R.id.btNoteUpdate)

        //OnClick Section
        btUpdate.setOnClickListener {
            val noteId = sharedPreferences.getString("NoteId", "").toString()

            //Validate the user input
            if(etNote.text.toString().isNotEmpty()) {
                updateViewModel.editNote(noteId, etNote.text.toString())
            }

            with(sharedPreferences.edit()){
                putString("NoteId", etNote.text.toString())
                apply()
            }
            findNavController().navigate(R.id.action_fragment_update_to_fragment_list)
        }
        return view
    }
}