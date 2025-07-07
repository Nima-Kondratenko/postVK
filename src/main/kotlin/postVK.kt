data class Post(
    val id: Int = 0, // значение по умолчанию
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val replyOwnerId: Int? = null, // Nullable: может не быть, если пост не является ответом
    val replyPostId: Int? = null, // Nullable: может не быть, если пост не является ответом
    val friendsOnly: Boolean = false,
    val comments: Comments? = null, // Nullable: может быть отсутствующим
    val copyright: Copyright? = null, // Nullable: может быть отсутствующим
    val likes: Likes = Likes(),
    val reposts: Reposts = Reposts(),
    val views: Views = Views(),
    val postType: String,
    val postSource: PostSource? = null, // Nullable: может быть отсутствующим
    val attachments: List<Attachment>? = null, // Nullable: может быть отсутствующим
    val geo: Geo? = null, // Nullable: может быть отсутствующим
    val signerId: Int? = null, // Nullable: может не быть, если пост не подписан
    val copyHistory: List<CopyHistory>? = null // Nullable: может быть отсутствующим
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

interface Attachment {
    val type: String // Тип вложения
}

data class PhotoAttachment(val photo: Photo) : Attachment {
    override val type: String = "photo"
}

data class AudioAttachment(val audio: Audio) : Attachment {
    override val type: String = "audio"
}

data class VideoAttachment(val video: Video) : Attachment {
    override val type: String = "video"
}

data class Photo(
    val id: Int,
    val albumId: Int,
    val ownerId: Int,
    val userId: Int,
    val text: String,
    val date: Int,
    val sizes: List<Size>
)

data class Size(
    val height: Int,
    val width: Int,
    val url: String,
    val type: String // тип размера (например, "s", "m", "x")
)

data class Audio(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val artist: String,
    val duration: Int,
    val url: String
)

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String,
    val duration: Int,
    val link: String
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
    private var posts = emptyArray<Post>()
    private var nextId = 1 // Переменная для хранения следующего уникального ID

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId)
        posts += newPost
        nextId++
        return newPost
    }

    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            posts[index] = post.copy()
            true
        } else {
            false
        }
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1 // Сбрасываем счетчик ID
    }

    fun getPosts(): List<Post> {
        return posts.toList()
    }
}

fun main() {
    // Добавляем посты с вложениями
    val photoAttachment = PhotoAttachment(
        Photo(
            id = 1,
            albumId = 1,
            ownerId = 123,
            userId = 1,
            text = "фото",
            date = 1633046400,
            sizes = listOf(Size(100, 100, "http://example.com/photo.jpg", "m"))
        )
    )

    // Пример создания поста с вложениями
    val post1 = WallService.add(
        Post(
            ownerId = 123,
            fromId = 123,
            createdBy = 1,
            date = 1633046400,
            text = "Это первый пост с вложениями!",
            likes = Likes(count = 10, userLikes = true),
            comments = Comments(count = 2),
            reposts = Reposts(count = 1),
            views = Views(count = 100),
            postType = "post",
            attachments = listOf(photoAttachment) // Добавляем вложение в пост
        )
    )

    println(post1)
}
