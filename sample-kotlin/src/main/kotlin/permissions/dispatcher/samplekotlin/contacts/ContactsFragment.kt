package permissions.dispatcher.samplekotlin.contacts

import android.content.ContentProviderOperation
import android.content.OperationApplicationException
import android.database.Cursor
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import permissions.dispatcher.samplekotlin.R
import kotlin.properties.Delegates

/**
 * Displays the first contact stored on the device and contains an option to add a dummy contact.
 *
 *
 * This Fragment is only used to illustrate that access to the Contacts ContentProvider API has
 * been granted (or denied) as part of the runtime permissions model. It is not relevant for the
 * use
 * of the permissions API.
 *
 *
 * This fragments demonstrates a basic use case for accessing the Contacts Provider. The
 * implementation is based on the training guide available here:
 * https://developer.android.com/training/contacts-provider/retrieve-names.html
 */
class ContactsFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var messageText: TextView by Delegates.notNull<TextView>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_contacts, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        messageText = view.findViewById(R.id.contact_message)

        val button: Button? = view.findViewById(R.id.back)
        button?.setOnClickListener {
            fragmentManager.popBackStack()
        }

        val addButton: Button = view.findViewById(R.id.contact_add)
        addButton.setOnClickListener {
            insertDummyContact()
        }
        val loadButton: Button = view.findViewById(R.id.contact_load)
        loadButton.setOnClickListener {
            loadContact()
        }
    }

    /**
     * Restart the Loader to query the Contacts content provider to display the first contact.
     */
    private fun loadContact() = loaderManager.restartLoader(0, null, this)

    /**
     * Initialises a new [CursorLoader] that queries the [ContactsContract].
     */
    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> = CursorLoader(activity, ContactsContract.Contacts.CONTENT_URI, PROJECTION, null, null, ORDER)

    /**
     * Dislays either the name of the first contact or a message.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursor?.let {
            val totalCount = it.count
            if (totalCount > 0) {
                it.moveToFirst()
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                messageText.text = resources.getString(R.string.contacts_string, totalCount, name)

                Log.d(TAG, "First contact loaded: " + name)
                Log.d(TAG, "Total number of contacts: " + totalCount)
                Log.d(TAG, "Total number of contacts: " + totalCount)
            } else {
                Log.d(TAG, "List of contacts is empty.")
                messageText.setText(R.string.contacts_empty)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) = messageText.setText(R.string.contacts_empty)

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     *
     *
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private fun insertDummyContact() {
        // Two operations are needed to insert a new contact.
        val operations = arrayListOf<ContentProviderOperation>()

        // First, set up a new raw contact.
        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build().let { operations.add(it) }


        // Next, set the name for the contact.
        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME)
                .build().let { operations.add(it) }

        // Apply the operations.
        try {
            activity.contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
        } catch (e: RemoteException) {
            Log.d(TAG, "Could not add a new contact: " + e.message)
        } catch (e: OperationApplicationException) {
            Log.d(TAG, "Could not add a new contact: " + e.message)
        }
    }

    companion object {
        private val TAG = "Contacts"

        private val DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample"

        /**
         * Projection for the content provider query includes the id and primary name of a contact.
         */
        private val PROJECTION = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        /**
         * Sort order for the query. Sorted by primary name in ascending order.
         */
        private val ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"

        /**
         * Creates a new instance of a ContactsFragment.
         */
        fun newInstance(): ContactsFragment = ContactsFragment()
    }
}
