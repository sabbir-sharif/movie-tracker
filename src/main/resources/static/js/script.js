/**
 * ================================
 * GLOBAL CONFIG
 * ================================
 */
const API_URL = "http://localhost:8080/movies";
let currentPage = 0;
let currentSize = 5;
let currentMovies = [];

/**
 * ================================
 * DOM ELEMENTS
 * ================================
 */
const movieForm = document.getElementById('movieForm');
const movieTableBody = document.getElementById('movieTableBody');
const submitBtn = document.getElementById('submitBtn');
const pageInfo = document.getElementById('pageInfo');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const pageSizeSelect = document.getElementById('pageSize');

/**
 * ================================
 * FETCH WITH SESSION
 * ================================
 */
function fetchWithSession(url, options = {}) {
    return fetch(url, {
        credentials: "include", // 🔥 REQUIRED for HttpSession
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });
}

/**
 * ================================
 * FETCH MOVIES
 * ================================
 */
async function fetchMovies() {
    try {
        const response = await fetchWithSession(
            `${API_URL}?page=${currentPage}&size=${currentSize}`
        );

        if (response.status === 401) {
            redirectToLogin();
            return;
        }

        if (!response.ok) throw new Error("Failed to fetch movies");

        const data = await response.json();
        currentMovies = data.content || [];

        renderTable(currentMovies);
        updatePaginationControls(data);

    } catch (error) {
        console.error("Fetch Error:", error);
        movieTableBody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align:center; color:red;">
                    Error: ${error.message}
                </td>
            </tr>`;
    }
}

/**
 * ================================
 * RENDER TABLE
 * ================================
 */
function renderTable(movies) {
    movieTableBody.innerHTML = '';

    if (!movies || movies.length === 0) {
        movieTableBody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align:center;">
                    No movies found.
                </td>
            </tr>`;
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
 * ================================
 * PREPARE UPDATE
 * ================================
 */
function prepareUpdate(id) {
    const movie = currentMovies.find(m => m.id === id);

    if (!movie) {
        console.error("Movie not found:", id);
        return;
    }

    document.getElementById('movieId').value = movie.id;
    document.getElementById('title').value = movie.title;
    document.getElementById('genre').value = movie.genre;
    document.getElementById('status').value = movie.status;

    submitBtn.innerText = "Update Movie";
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * ================================
 * ADD / UPDATE MOVIE
 * ================================
 */
movieForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('movieId').value;

    const movieData = {
        title: document.getElementById('title').value.trim(),
        genre: document.getElementById('genre').value.trim(),
        status: document.getElementById('status').value.trim()
    };

    const isUpdate = id !== "";
    const url = isUpdate ? `${API_URL}/${id}` : API_URL;
    const method = isUpdate ? "PUT" : "POST";

    try {
        const response = await fetchWithSession(url, {
            method: method,
            body: JSON.stringify(movieData)
        });

        if (response.status === 401) {
            redirectToLogin();
            return;
        }

        if (!response.ok) throw new Error("Save failed");

        resetForm();
        fetchMovies();

    } catch (error) {
        console.error("Save Error:", error);
        alert("Error saving movie.");
    }
});

/**
 * ================================
 * DELETE MOVIE
 * ================================
 */
async function deleteMovie(id) {
    if (!confirm("Are you sure you want to delete this movie?")) return;

    try {
        const response = await fetchWithSession(`${API_URL}/${id}`, {
            method: "DELETE"
        });

        if (response.status === 401) {
            redirectToLogin();
            return;
        }

        if (!response.ok) throw new Error("Delete failed");

        if (currentMovies.length === 1 && currentPage > 0) {
            currentPage--;
        }

        fetchMovies();

    } catch (error) {
        console.error("Delete Error:", error);
    }
}

/**
 * ================================
 * PAGINATION
 * ================================
 */
function updatePaginationControls(data) {
    currentPage = data.number;

    pageInfo.innerText = `Page ${data.number + 1} of ${data.totalPages || 1}`;

    prevBtn.disabled = data.first;
    nextBtn.disabled = data.last || data.totalPages === 0;
}

function changePage(direction) {
    currentPage += direction;
    fetchMovies();
}

function changeSize() {
    currentSize = parseInt(pageSizeSelect.value);
    currentPage = 0;
    fetchMovies();
}

/**
 * ================================
 * HELPERS
 * ================================
 */
function resetForm() {
    movieForm.reset();
    document.getElementById('movieId').value = "";
    submitBtn.innerText = "Add Movie";
}

function redirectToLogin() {
    alert("Session expired. Please login again.");
    window.location.href = "/auth.html";
}

/**
 * ================================
 * LOGOUT
 * ================================
 */
function handleLogout() {
    fetch("http://localhost:8080/auth/logout", {
        method: "POST",
        credentials: "include"
    }).finally(() => {
        window.location.href = "/auth.html";
    });
}

/**
 * ================================
 * INIT APP
 * ================================
 */
document.addEventListener('DOMContentLoaded', () => {
    fetchMovies();
});