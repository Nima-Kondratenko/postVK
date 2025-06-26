data class Post(
    val id: Int = 0, // Установим значение по умолчанию
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val replyOwnerId: Int? = null,
    val replyPostId: Int? = null,
    val friendsOnly: Boolean = false,
    val comments: Comments = Comments(),
    val copyright: Copyright? = null,
    val likes: Likes = Likes(),
    val reposts: Reposts = Reposts(),
    val views: Views = Views(),
    val postType: String,
    val postSource: PostSource? = null,
    val attachments: List<Attachment> = emptyList(),
    val geo: Geo? = null,
    val signerId: Int? = null,
    val copyHistory: List<CopyHistory> = emptyList()
)

data class Comments(
    val count: Int = 0,
    val canPost: Boolean = false,
    val groupsCanPost: Boolean = false,
    val canClose: Boolean = false,
    val canOpen: Boolean = false
)

data class Copyright(
    val id: Int,
    val link: String? = null,
    val name: String? = null,
    val type: String? = null
)

data class Likes(
    val count: Int = 0,
    val userLikes: Boolean = false,
    val canLike: Boolean = true,
    val canPublish: Boolean = true
)

data class Reposts(
    val count: Int = 0,
    val userReposted: Boolean = false
)

data class Views(
    val count: Int = 0
)

data class PostSource(
    // Пример поля для описания источника размещения записи
    val type: String? = null
)

data class Attachment(
    // Пример поля для описания медиаресурсов
    val type: String? = null
)

data class Geo(
    val type: String? = null,
    val coordinates: String? = null,
    val place: Place? = null
)

data class Place(
    // Пример поля для описания места
    val title: String? = null
)

data class CopyHistory(
    // Пример поля для описания истории копирования записи
    val id: Int? = null
)

object WallService {
    private var posts = mutableListOf<Post>()
    private var nextId = 1 // Переменная для хранения следующего уникального ID

    fun add(post: Post): Post {
        // Устанавливаем уникальный ID для нового поста
        val newPost = post.copy(id = nextId)
        posts.add(newPost)
        nextId++ // Увеличиваем ID для следующего поста
        return newPost
    }

    fun update(post: Post): Boolean {
        // Находим индекс поста с таким же ID
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            // Если пост найден, обновляем его свойства
            posts[index] = post.copy() // Копируем все свойства из переданного поста
            true
        } else {
            false // Пост с таким ID не найден
        }
    }

    fun getPosts(): List<Post> {
        return posts
    }
}

fun main() {
    // Добавляем посты
    val post1 = WallService.add(
        Post(
            ownerId = 123,
            fromId = 123,
            createdBy = 1,
            date = 1633046400,
            text = "Это первый пост!",
            likes = Likes(count = 10, userLikes = true),
            comments = Comments(count = 2),
            reposts = Reposts(count = 1),
            views = Views(count = 100),
            postType = "post"
        )
    )

    val post2 = WallService.add(
        Post(
            ownerId = 124,
            fromId = 124,
            createdBy = 1,
            date = 1633046500,
            text = "Это второй пост!",
            likes = Likes(count = 5, userLikes = false),
            comments = Comments(count = 3),
            reposts = Reposts(count = 0),
            views = Views(count = 50),
            postType = "post"
        )
    )

    // Обновление первого поста с правильным ID
    val updatedPost1 = post1.copy(text = "Это обновленный первый пост!")
    val isUpdated = WallService.update(updatedPost1)

    println("Пост обновлен: $isUpdated")

    // Вывод всех постов
    val allPosts = WallService.getPosts()
    for (post in allPosts) {
        println("Пост ID: ${post.id}")
        println("Текст: ${post.text}")
        println("Лайков: ${post.likes.count}, Комментариев: ${post.comments.count}, Репостов: ${post.reposts.count}, Просмотров: ${post.views.count}")
        println("-----")
    }
}
