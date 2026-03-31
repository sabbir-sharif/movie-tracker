/**
 * Global State & Configuration
 */
const API_URL = "http://localhost:8080/movies";
let currentPage = 0;
let currentSize = 10;
let currentMovies = []; // MAGIC FIX: Stores the current page of movies to avoid string errors

// DOM Elements
const movieForm = document.getElementById('movieForm');
const movieTableBody = document.getElementById('movieTableBody');
const submitBtn = document.getElementById('submitBtn');
const pageInfo = document.getElementById('pageInfo');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const pageSizeSelect = document.getElementById('pageSize');

/**
 * 1. FETCH & DISPLAY
 */
async function fetchMovies() {
    try {
        const response = await fetch(`${API_URL}?page=${currentPage}&size=${currentSize}`);

        if (!response.ok) throw new Error("The owl lost the message (Server Error)");

        const data = await response.json();

        // Save the list of movies globally so "Edit" can find them by ID
        currentMovies = data.content;

        renderTable(currentMovies);
        updatePaginationControls(data);
    } catch (error) {
        console.error("Scroll Error:", error);
        movieTableBody.innerHTML = `<tr><td colspan="4" style="text-align:center; color:red;">Dark Magic detected: ${error.message}</td></tr>`;
    }
}

function renderTable(movies) {
    movieTableBody.innerHTML = '';

    if (movies.length === 0) {
        movieTableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">The chronicle is empty.</td></tr>';
        return;
    }

    movies.forEach(movie => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${movie.title}</td>
            <td>${movie.genre}</td>
            <td><span class="status-tag">${movie.status}</span></td>
            <td>
                <button class="btn-edit" onclick="prepareUpdate(${movie.id})">Edit</button>
                <button class="btn-delete" onclick="deleteMovie(${movie.id})">Delete</button>
            </td>
        `;
        movieTableBody.appendChild(row);
    });
}

/**
 * 2. EDIT LOGIC (The "Repairing Charm")
 */
function prepareUpdate(id) {
    // Find the movie object from our global array using the ID
    const movie = currentMovies.find(m => m.id === id);

    if (movie) {
        // Fill the form with the movie data
        document.getElementById('movieId').value = movie.id;
        document.getElementById('title').value = movie.title;
        document.getElementById('genre').value = movie.genre;
        document.getElementById('status').value = movie.status;

        // Change button text to indicate we are updating
        submitBtn.innerText = "Cast Update";

        // Smooth scroll back to the top form
        window.scrollTo({ top: 0, behavior: 'smooth' });
    } else {
        console.error("Could not find movie with ID:", id);
    }
}

/**
 * 3. CREATE & UPDATE (POST/PUT)
 */
movieForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('movieId').value;
    const movieData = {
        title: document.getElementById('title').value,
        genre: document.getElementById('genre').value,
        status: document.getElementById('status').value
    };

    // If ID exists, we use PUT (Update). If not, we use POST (New).
    const isUpdate = id !== "";
    const url = isUpdate ? `${API_URL}/${id}` : API_URL;
    const method = isUpdate ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movieData)
        });

        if (response.ok) {
            resetForm();
            fetchMovies(); // Refresh table
        } else {
            alert("Spell failed! Check server logs.");
        }
    } catch (error) {
        console.error("Save error:", error);
    }
});

/**
 * 4. DELETE
 */
async function deleteMovie(id) {
    if (!confirm("Are you sure you want to vanish this entry?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        if (response.ok) {
            // If we delete the last item on a page, go back one page
            if (currentMovies.length === 1 && currentPage > 0) {
                currentPage--;
            }
            fetchMovies();
        }
    } catch (error) {
        console.error("Vanish error:", error);
    }
}

/**
 * 5. PAGINATION NAVIGATION
 */
function updatePaginationControls(data) {
    currentPage = data.number;
    pageInfo.innerText = `Scroll ${data.number + 1} of ${data.totalPages || 1}`;

    prevBtn.disabled = data.first;
    nextBtn.disabled = data.last || data.totalPages === 0;
}

function changePage(direction) {
    currentPage += direction;
    fetchMovies();
}

function changeSize() {
    currentSize = pageSizeSelect.value;
    currentPage = 0; // Always reset to page 1 when resizing
    fetchMovies();
}

/**
 * HELPER FUNCTIONS
 */
function resetForm() {
    movieForm.reset();
    document.getElementById('movieId').value = "";
    submitBtn.innerText = "Cast Entry";
}

function handleLogout() {
  // your logout logic here (clear session, redirect, etc.)
  window.location.href = "/login";
}

// Start the magic when the page loads
document.addEventListener('DOMContentLoaded', fetchMovies);