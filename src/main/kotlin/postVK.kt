data class Post(
    val id: Int = 0,
    val ownerId: Int,
    val fromId: Int,
    val createdBy: Int,
    val date: Int,
    val text: String,
    val replyOwnerId: Int? = null,
    val replyPostId: Int? = null,
    val friendsOnly: Boolean = false,
    val comments: Comments? = null,
    val copyright: Copyright? = null,
    val likes: Likes = Likes(),
    val reposts: Reposts = Reposts(),
    val views: Views = Views(),
    val postType: String,
    val postSource: PostSource? = null,
    val attachments: List<Attachment>? = null,
    val geo: Geo? = null,
    val signerId: Int? = null,
    val copyHistory: List<CopyHistory>? = null
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

data class Comment(
    val id: Int,
    val postId: Int,
    val ownerId: Int,
    val date: Int,
    val text: String
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

class PostNotFoundException(message: String) : Exception(message)

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()

    private var nextId = 1 // Переменная для хранения следующего уникального ID поста
    private var nextCommentId = 1 // Переменная для хранения следующего уникального ID комментария

    fun add(post: Post): Post {
        // Создаем новый пост с уникальным ID
        val newPost = post.copy(id = nextId)
        posts += newPost
        nextId++
        return newPost
    }

    fun createComment(postId: Int, commentText: String, ownerId: Int): Comment {
        // Проверяем, существует ли пост с данным ID
        val postIndex = posts.indexOfFirst { it.id == postId }
        if (postIndex == -1) {
            throw PostNotFoundException("Пост с ID $postId не найден.")
        }

        // Создаем новый комментарий
        val newComment = Comment(
            id = nextCommentId,
            postId = postId,
            ownerId = ownerId,
            date = System.currentTimeMillis().toInt() / 1000, // Текущая дата в формате UNIX timestamp
            text = commentText
        )

        // Добавляем комментарий в массив комментариев
        comments += newComment
        nextCommentId++

        // Обновляем количество комментариев в посте безопасно
        posts[postIndex] = posts[postIndex].copy(
            comments = posts[postIndex].comments?.let {
                it.copy(count = it.count + 1)
            } ?: Comments(count = 1) // Если comments == null, создаем новый объект Comments с count=1
        )

        return newComment
    }

    fun update(post: Post): Boolean {
        // Обновляем пост по его ID
        val index = posts.indexOfFirst { it.id == post.id }
        return if (index != -1) {
            posts[index] = post.copy()
            true
        } else {
            false
        }
    }

    fun clear() {
        // Очищаем все посты и комментарии
        posts = emptyArray()
        comments = emptyArray()
        nextId = 1
        nextCommentId = 1
    }
}
