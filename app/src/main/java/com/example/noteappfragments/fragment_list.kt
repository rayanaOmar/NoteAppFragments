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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.*



class fragment_list : Fragment() {
    private lateinit var rvNotes: RecyclerView
    private lateinit var rvAdapter: NoteAdapter
    private lateinit var editText: EditText
    private lateinit var submitBtn: Button
    private lateinit var notes: List<Note>

    lateinit var listViewModel: ListViewModel
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //To access MainActivity here
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        //arrayList to store the notes user enter
        notes = arrayListOf()

        //listViewModel
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.getNotes().observe(viewLifecycleOwner, {
            notes -> rvAdapter.update(notes)
        })

        //initialization
        editText = view.findViewById(R.id.tvNewNote)
        submitBtn = view.findViewById(R.id.btSubmit)

        //RecyclerView adapter section
        rvNotes = view.findViewById(R.id.rvNotes)
        rvAdapter = NoteAdapter(this)
        rvNotes.adapter = rvAdapter
        rvNotes.layoutManager = LinearLayoutManager(requireContext())

        //OnClick Section
        submitBtn.setOnClickListener {

            //Validate the user input
            if(editText.text.toString().isNotEmpty()) {
                listViewModel.addNote(Note("", editText.text.toString()))
            }
            editText.text.clear()
            editText.clearFocus()
        }

        listViewModel.getData()

        return view
    }

    override fun onResume() {
        super.onResume()
        // We call the 'getData' function from our ViewModel after a one second delay because Firestore takes some time
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            listViewModel.getData()
        }
    }
}