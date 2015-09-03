/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package permissions.dispatcher.sample.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import permissions.dispatcher.sample.R;

import java.util.ArrayList;

/**
 * Displays the first contact stored on the device and contains an option to add a dummy contact.
 * <p>
 * This Fragment is only used to illustrate that access to the Contacts ContentProvider API has
 * been granted (or denied) as part of the runtime permissions model. It is not relevant for the
 * use
 * of the permissions API.
 * <p>
 * This fragments demonstrates a basic use case for accessing the Contacts Provider. The
 * implementation is based on the training guide available here:
 * https://developer.android.com/training/contacts-provider/retrieve-names.html
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Contacts";
    private TextView mMessageText = null;

    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";

    /**
     * Projection for the content provider query includes the id and primary name of a contact.
     */
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    /**
     * Sort order for the query. Sorted by primary name in ascending order.
     */
    private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";


    /**
     * Creates a new instance of a ContactsFragment.
     */
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        mMessageText = (TextView) rootView.findViewById(R.id.contact_message);

        // Register a listener to add a dummy contact when a button is clicked.
        Button button = (Button) rootView.findViewById(R.id.contact_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDummyContact();
            }
        });

        // Register a listener to display the first contact when a button is clicked.
        button = (Button) rootView.findViewById(R.id.contact_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadContact();
            }
        });
        return rootView;
    }

    /**
     * Restart the Loader to query the Contacts content provider to display the first contact.
     */
    private void loadContact() {
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * Initialises a new {@link CursorLoader} that queries the {@link ContactsContract}.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                null, null, ORDER);
    }


    /**
     * Dislays either the name of the first contact or a message.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null) {
            final int totalCount = cursor.getCount();
            if (totalCount > 0) {
                cursor.moveToFirst();
                String name = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mMessageText.setText(
                        getResources().getString(R.string.contacts_string, totalCount, name));
                Log.d(TAG, "First contact loaded: " + name);
                Log.d(TAG, "Total number of contacts: " + totalCount);
                Log.d(TAG, "Total number of contacts: " + totalCount);
            } else {
                Log.d(TAG, "List of contacts is empty.");
                mMessageText.setText(R.string.contacts_empty);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMessageText.setText(R.string.contacts_empty);
    }

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     * <p>
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME);
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getActivity().getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        } catch (OperationApplicationException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        }
    }
}
