package com.example.fariyafardinfarhancollection.ui.fragment.extra

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fariyafardinfarhancollection.*
import com.example.fariyafardinfarhancollection.database.ShopDatabase
import com.example.fariyafardinfarhancollection.databinding.DialogEditEmployeeProfileBinding
import com.example.fariyafardinfarhancollection.databinding.DialogEditPublicPostBinding
import com.example.fariyafardinfarhancollection.databinding.DialogUpsertCustomerContactBinding
import com.example.fariyafardinfarhancollection.databinding.FragmentExtraBinding
import com.example.fariyafardinfarhancollection.model.Employee
import com.example.fariyafardinfarhancollection.model.PublicPost
import com.example.fariyafardinfarhancollection.model.StoreProduct
import com.example.fariyafardinfarhancollection.notification.NotificationData
import com.example.fariyafardinfarhancollection.notification.PushNotification
import com.example.fariyafardinfarhancollection.notification.RetrofitInstance
import com.example.fariyafardinfarhancollection.repository.ShopRepository
import com.example.fariyafardinfarhancollection.ui.LoginActivity
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModel
import com.example.fariyafardinfarhancollection.viewmodel.ShopViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ExtraFragment : Fragment() {

    private var _binding: FragmentExtraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val publicPostAdapter by lazy { PublicPostAdapter() }

    private lateinit var shopViewModel: ShopViewModel

    private var currentEmployee: Employee? = null

    private lateinit var documentSnapshot: DocumentReference

    var employeeImageUri: Uri? = null
    var employeeNidUri: Uri? = null
    val imageReference = Firebase.storage.reference

    private val publicPostsCollectionRef = Firebase.firestore.collection("publicPosts")
    private val publicPostsCounterCollectionRef = Firebase.firestore.collection("allCounters")

    private var databasePostsCounter: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExtraBinding.bind(view)

        auth = Firebase.auth
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val shopDao = ShopDatabase.getDatabase(requireContext()).shopDao()
        val shopRepository = ShopRepository(shopDao)
        val shopViewModelProviderFactory =
            ShopViewModelProviderFactory(requireActivity().application, shopRepository)
        shopViewModel =
            ViewModelProvider(this, shopViewModelProviderFactory)[ShopViewModel::class.java]

        setEmployeeProfile()

        setUpPublicPostRecyclerView()

//        shopViewModel.getAllPublicPost.observe(viewLifecycleOwner, Observer {
//            publicPostAdapter.differ.submitList(it)
//        })

        setImagesFromStorage()

        publicPostsCollectionRef
            .orderBy("publicPostId", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
            error?.let {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            value?.let { querySnapshot ->
                val publicPostsList = querySnapshot.toObjects<PublicPost>()
                publicPostAdapter.differ.submitList(publicPostsList)
            }
        }

        binding.imgEmployeeProfile.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, EMPLOYEE_IMAGE_REQUEST_CODE)
            }
        }
        binding.imgAddNid.setOnClickListener {
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                startActivityForResult(it, NID_IMAGE_REQUEST_CODE)
            }
        }

        binding.txtSignOut.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Signing off...")
            builder.setNegativeButton("Cancel") { _, _ -> }
            builder.setPositiveButton("Continue") { _, _ ->
                auth.signOut()
                activity?.finish()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }.create().show()
        }
        binding.btnCurrencyConverter.setOnClickListener {
            findNavController().navigate(R.id.action_extraFragment_to_currencyConverterFragment)
        }

        binding.imgEditProfile.setOnClickListener {
            setUpEditProfile()
        }

        // TODO: needs to provide user specific visibility for updation or deletion
        /*publicPostAdapter.setOnItemClickListener(object : PublicPostAdapter.OnItemClickListener{
            override fun onEditClick(publicPost: PublicPost) {
                val inflater = LayoutInflater.from(requireContext())
                val ppBinding = DialogEditPublicPostBinding.inflate(inflater)

                ppBinding.edtPost.setText(publicPost.post)

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(ppBinding.root)
                builder.setNegativeButton("Cancel"){_,_->}
                builder.setPositiveButton("Done"){_,_->
                    val updatedPost = ppBinding.edtPost.text.toString()
                    shopViewModel.updatePublicPost(PublicPost(publicPost.publicPostId,publicPost.employeeName, publicPost.dateAndTime, updatedPost))
                    Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT).show()
                }
                builder.create().show()
            }

            override fun onDeleteClick(publicPost: PublicPost) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete post?")
                builder.setNegativeButton("No"){_,_->}
                builder.setPositiveButton("Yes"){_,_->
                    shopViewModel.deletePublicPost(publicPost)
                    Toast.makeText(requireContext(), "Post deleted successfully!", Toast.LENGTH_SHORT).show()
                }
                builder.create().show()
            }

        })*/
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
//                if (response.isSuccessful) {
//                    Log.d("ExtraFragment", "Response: ${Gson().toJson(response)}")
//                }
            } catch (e: Exception) {
                Log.e("ExtraFragment", e.toString())
            }

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EMPLOYEE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let {
                employeeImageUri = it
                binding.imgEmployeeProfile.setImageURI(it)
                uploadProfileImageToStorage()
            }
        }
        if (requestCode == NID_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data.let {
                employeeNidUri = it
                binding.imgAddNid.setImageURI(it)
                uploadNidImageToStorage()
            }
        }
    }

    private fun setImagesFromStorage() {
        val profileReference =
            imageReference.child("empImages/${auth.currentUser!!.uid}/profileImage")
        val profileLocalFile = File.createTempFile("profileFile", ".jpg")
        profileReference.getFile(profileLocalFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(profileLocalFile.absolutePath)
                bitmap?.let {
                    binding.imgEmployeeProfile.setImageBitmap(it)
                }
            }

        val nidReference = imageReference.child("empImages/${auth.currentUser!!.uid}/nidImage")
        val nidLocalFile = File.createTempFile("nidFile", ".jpg")
        nidReference.getFile(nidLocalFile)
            .addOnSuccessListener {
                binding.textNidText.setTextColor(resources.getColor(R.color.green))
                val bitmap = BitmapFactory.decodeFile(nidLocalFile.absolutePath)
                binding.imgAddNid.setImageBitmap(bitmap)
            }
    }

    private fun uploadProfileImageToStorage() {
        employeeImageUri?.let {
            imageReference.child("empImages/${auth.currentUser!!.uid}/profileImage").putFile(it)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Successfully uploaded image!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Image Upload Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun uploadNidImageToStorage() {
        employeeNidUri?.let {
            imageReference.child("empImages/${auth.currentUser!!.uid}/nidImage").putFile(it)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Successfully uploaded image!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Image Upload Failed!", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun setUpEditProfile() {
        val inflater = LayoutInflater.from(requireContext())
        val epBinding = DialogEditEmployeeProfileBinding.inflate(inflater)
        epBinding.edtEmployeeName.setText(currentEmployee?.username)
        epBinding.edtContact.setText(currentEmployee?.contact)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(epBinding.root)
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Submit") { _, _ ->
            if (epBinding.edtContact.text.toString() != "" && epBinding.edtEmployeeName.text.toString() != "") {
                val updatedName = epBinding.edtEmployeeName.text.toString()
                val updatedContact = epBinding.edtContact.text.toString()
                val updateEmployee = mapOf(
                    "contact" to updatedContact,
                    "username" to updatedName
                )
                documentSnapshot.update(updateEmployee)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Successfully updated!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        builder.create().show()
    }

    private fun setEmployeeProfile() {
        documentSnapshot =
            Firebase.firestore.collection("registeredEmployees").document(auth.currentUser!!.uid)
        documentSnapshot.get().addOnSuccessListener {
            currentEmployee = it.toObject<Employee>()
            currentEmployee?.let { employee ->
                // TODO: app crashes if bottom nav changes before it can set all the data
                binding.textEmployeeName.text = employee.username
                binding.textEmployeeEmail.text = employee.email
                binding.textEmployeeContact.text = employee.contact
            }
        }
    }

    private fun setUpPublicPostRecyclerView() {
        val rvPublicPost = binding.rvPublicPost
        rvPublicPost.adapter = publicPostAdapter
        rvPublicPost.layoutManager = LinearLayoutManager(requireContext())

        binding.btnCreatePost.setOnClickListener {
            val calender = Calendar.getInstance()
            val time = calender.time

            val employeeName = currentEmployee?.username
            val dateAndTime = "$time"
            val post = binding.edtCreatePost.text.toString()

            PushNotification(
                NotificationData(employeeName!!, post),
                TOPIC
            ).also {
                sendNotification(it)
            }

            Firebase.firestore.runTransaction { transaction->
                val counterRef = publicPostsCounterCollectionRef.document("publicPostsCounter")
                val counter = transaction.get(counterRef)
                val newCounter = counter["publicPostId"] as Long + 1
                databasePostsCounter = newCounter.toInt()
                transaction.update(counterRef,"publicPostId", newCounter)

                publicPostsCollectionRef.document().set(PublicPost(newCounter.toInt(), employeeName, dateAndTime, post))
                null
            }.addOnSuccessListener {
//              shopViewModel.insertPublicPost(PublicPost(0, employeeName, dateAndTime, post))
                binding.edtCreatePost.setText("")
                Toast.makeText(requireContext(), "Successful!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}