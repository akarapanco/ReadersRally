const { initializeApp, cert } = require('firebase-admin/app');
const { getFirestore } = require('firebase-admin/firestore');
const serviceAccount = require('./readersrally.json');

initializeApp({ credential: cert(serviceAccount) });
const db = getFirestore();

async function setup() {
  await db.collection('groups').add({
    name: 'Test Book Club', genre: 'Fiction', bookTitle: 'Example Book',
    communicationMethod: 'online', groupSize: 8, currentMembers: 1, createdBy: 'testUser'
  });
  await db.collection('users').add({
    name: 'Test User', email: 'test@ufl.edu', enrolledGroupIds: [], bookmarkedGroupIds: []
  });
  await db.collection('joinRequests').add({
    groupId: 'placeholder', userId: 'placeholder', message: '', status: 'pending'
  });
  console.log('Collections created.');
}

setup();
