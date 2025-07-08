import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith

class WallServiceTest {

    @Before
    fun clearBeforeTest() {
        WallService.clear()
    }

    @Test
    fun addPostShouldAssignNonZeroId() {
        val post = Post(
            ownerId = 123,
            fromId = 123,
            createdBy = 1,
            date = 1633046400,
            text = "Тестовый пост",
            likes = Likes(count = 0),
            comments = Comments(count = 0),
            reposts = Reposts(count = 0),
            views = Views(count = 0),
            postType = "post" // Убедитесь, что этот параметр указан
        )

        val addedPost = WallService.add(post)

        // Проверяем, что ID добавленного поста не равен 0
        assertTrue(addedPost.id != 0)
    }

    @Test
    fun updateExistingPostShouldReturnTrue() {
        val post = WallService.add(Post(
            ownerId = 123,
            fromId = 123,
            createdBy = 1,
            date = 1633046400,
            text = "Первый пост",
            likes = Likes(count = 0),
            comments = Comments(count = 0),
            reposts = Reposts(count = 0),
            views = Views(count = 0),
            postType = "post" // Убедитесь, что этот параметр указан
        ))

        val updatedPost = post.copy(text = "Обновленный текст")

        // Проверяем, что обновление существующего поста возвращает true
        assertTrue(WallService.update(updatedPost))
    }

    @Test
    fun updateNonExistingPostShouldReturnFalse() {
        val nonExistentPost = Post(
            id = 999, // Устанавливаем несуществующий ID
            ownerId = 123,
            fromId = 123,
            createdBy = 1,
            date = 1633046400,
            text = "Не существующий пост",
            likes = Likes(count = 0),
            comments = Comments(count = 0),
            reposts = Reposts(count = 0),
            views = Views(count = 0),
            postType = "post" // Убедитесь, что этот параметр указан
        )

        // Проверяем, что обновление несуществующего поста возвращает false
        assertFalse(WallService.update(nonExistentPost))
    }

    @Test
    fun shouldAddCommentToExistingPost() {
        // Arrange
        val post = Post(
            ownerId = 1,
            fromId = 1,
            createdBy = 1,
            date = 1234567890,
            text = "Hello World",
            likes = Likes(count = 0),
            comments = Comments(count = 0),
            reposts = Reposts(count = 0),
            views = Views(count = 0),
            postType = "post" // Добавлено значение для postType
        )
        WallService.add(post)

        // Act
        val commentText = "Nice post!"
        val comment = WallService.createComment(postId = post.id, commentText = commentText, ownerId = 2)

        // Assert
        assertEquals(comment.text, commentText)
        assertEquals(comment.postId, post.id)
    }

    @Test
    fun shouldThrowWhenAddingCommentToNonExistentPost() {
        // Arrange
        val nonExistentPostId = 999 // ID поста, который не существует

        // Act & Assert
        assertFailsWith<PostNotFoundException> {
            WallService.createComment(postId = nonExistentPostId, commentText = "This post does not exist", ownerId = 2)
        }
    }
}
