let currentSlide = 0;
const slides = document.querySelectorAll(".slide-item");

function showSlide(index) {
    slides.forEach((slide, i) => {
        slide.classList.toggle("active", i === index);
    });
}

function changeSlide(step) {
    currentSlide = (currentSlide + step + slides.length) % slides.length;
    showSlide(currentSlide);
}

function playVideo(index) {
    const video = slides[index].querySelector(".slide-video");
    video.play();
}

showSlide(currentSlide);

// ----------------------------------------------------------------
document.querySelectorAll('.streaming-item').forEach(item => {
    const video = item.querySelector('.streaming-video');

    item.addEventListener('mouseover', () => {
        video.play();
    });

    item.addEventListener('mouseleave', () => {
        video.pause();
        video.currentTime = 0;
    });
});
// ------------------------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("searchInput");
    const searchResults = document.getElementById("searchResults");

    searchInput.addEventListener("input", () => {
        const query = searchInput.value.trim();

        if (query.length > 2) {
            fetch(`https://www.omdbapi.com/?s=${query}&apikey=882b9bb9`)
                .then(response => response.json())
                .then(data => {
                    if (data.Search) {
                        searchResults.innerHTML = data.Search.map(movie => `
                            <div class="movie-item" onclick="showMovieDetails('${movie.imdbID}')">
                                <img src="${movie.Poster}" alt="${movie.Title}">
                                <div class="movie-info">
                                    <h4>${movie.Title}</h4>
                                    <p>${movie.Year}</p>
                                </div>
                            </div>
                        `).join("");

                        searchResults.style.display = "block";
                    } else {
                        searchResults.innerHTML = "<p style='padding: 10px;'>No results found</p>";
                        searchResults.style.display = "block";
                    }
                });
        } else {
            searchResults.style.display = "none";
        }
    });

    // Hide results if clicked outside
    document.addEventListener("click", (event) => {
        if (!searchResults.contains(event.target) && !searchInput.contains(event.target)) {
            searchResults.style.display = "none";
        }
    });

    window.showMovieDetails = (imdbID) => {
        alert(`Redirect to movie details: ${imdbID}`);
        // You can replace this with navigation to the movie page
    };
});
