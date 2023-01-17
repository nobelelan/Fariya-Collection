package com.example.fariyafardinfarhancollection.practise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fariyafardinfarhancollection.R
import com.example.fariyafardinfarhancollection.databinding.ActivityPracticeBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPracticeBinding

    private val practiceCollectionRef = Firebase.firestore.collection("practiceCollections")
    private val practiceCollectionCounterRef = Firebase.firestore.collection("practiceCollectionsCounter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInsert.setOnClickListener {
            Firebase.firestore.runTransaction { transaction->
                val counterRef = practiceCollectionCounterRef.document("counter")
                val counter = transaction.get(counterRef)
                val newCounter = counter["counterId"] as Long + 1
                transaction.update(counterRef, "counterId", newCounter)

                val name = binding.edtName.text.toString()
                val age = binding.edtAge.text.toString()
                practiceCollectionRef.document().set(PracticeModel(newCounter.toInt(), name, age.toInt()))
                null
            }

        }
    }
}