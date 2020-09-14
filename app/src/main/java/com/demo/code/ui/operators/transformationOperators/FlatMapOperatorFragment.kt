package com.demo.code.ui.operators.transformationOperators

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.code.api.ServiceGenerator
import com.demo.code.api.ServiceGenerator.getRequestApi
import com.demo.code.databinding.FragmentOperatorFlatMapBinding
import com.demo.code.models.Comment
import com.demo.code.models.Post
import com.demo.code.ui.adapters.RecyclerAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


//URL for Displaying list of posts:-> jsonplaceholder.typicode.com/posts/
//URL for Displaying comments count for each post:-> jsonplaceholder.typicode.com/posts/1/comments/

class FlatMapOperatorFragment : Fragment() {

    private val TAG = FlatMapOperatorFragment::class.java.simpleName
    private var _binding: FragmentOperatorFlatMapBinding? = null
    private val binding get() = _binding!!

    private val disposables = CompositeDisposable()
    private val adapter = RecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOperatorFlatMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListeners()
        initializeList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeList() {
        adapter.apply {
            binding.contentFlatMap.recyclerView.layoutManager = LinearLayoutManager(activity)
            binding.contentFlatMap.recyclerView.adapter = adapter
        }
    }

    private fun onClickListeners() {
        binding.floatingActionButton.setOnClickListener {

            getListOfPosts()
                .subscribeOn(Schedulers.io())
                .flatMap { t ->
                    // Post object, but it is updated with the list of comments
                    getListOfComments(t)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Post> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onNext(post: Post) {
                        // Here we can update the post in the list
                        updatePost(post)
                    }

                    override fun onError(e: Throwable) {
                        Timber.tag(TAG).e("ERROR: %s", e.message)
                    }

                    override fun onComplete() {
                        TODO("Not yet implemented")
                    }

                })
        }
    }



    /**
     * Fetching list of posts from server
     * Here I want to convert one observable into list of observables
     * Display the list of posts to the list view since it is available
     * Convert a list of posts into observable list source of posts
     * Basically I am trying to emit the posts
     */
    private fun getListOfPosts(): Observable<Post> {
         return createPostListObservable()
            .subscribeOn(Schedulers.io())
             .flatMap { t ->
                 //Set the list to display the posts
                 adapter.setPosts(t as MutableList<Post>)
                 Observable.fromIterable(t).subscribeOn(Schedulers.io())
             }
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Update the adapter row by row
     **/
    private fun updatePost(post: Post) {
        Observable
            .fromIterable(adapter.getPosts())
            .filter { (_, id) -> id == post.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Post> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onNext(post: Post) {
                    Timber.tag(TAG)
                        .d("onNext: updating post: " + post.id + ", thread: " + Thread.currentThread().name)
                    adapter.updatePost(post)
                }

                override fun onError(e: Throwable) {
                    Timber.tag(TAG).e("ERROR: %s", e.message)
                }

                override fun onComplete() {}
            })
    }

    /**
     * Here we ar taking the post object as input
     * Using the id value in it get the list of comments from the comments api
     * Update the post object with comments list
     * Finally return the post observable object
     */
    private fun getListOfComments(post: Post) : Observable<Post>{
        return createCommentsListObservable(post.id)
            .map { comments ->
                post.comments = comments
                post
            }
            .subscribeOn(Schedulers.io())
    }


    /** ********************************* OBSERVABLES ********************************** **/
    /** Posts List **/
    private fun createPostListObservable(): Observable<List<Post>> {
        return getRequestApi().getPosts()
    }

    /** Comments List **/
    private fun createCommentsListObservable(id: Int): Observable<List<Comment>> {
        return getRequestApi().getComments(id)
    }
    /** ********************************* OBSERVABLES ********************************** **/
}