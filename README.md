# compose-animations
A collection of animations from popular apps like Instagram, YouTube, and Twitter with Jetpack Compose

# Instagram
<table>
  <th>pc upload complete</th>
  <th>heart</th>
  <th>dot indicator</th>

  <tr>
    <td align="center"><img src = "https://github.com/user-attachments/assets/e9e452e5-3116-45b3-a701-6fbf08e9f463" width = "250"></td>
    <td align="center"><img src = "https://github.com/user-attachments/assets/027a7934-3121-4ebb-a25d-3c7106252863" width = "250"></td>
    <td align="center"><img src = "https://github.com/user-attachments/assets/b9b12e44-5201-4718-8bd7-74bf2e083a8c" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
InstagramProgress(
    modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
    isComplete = true
)
```
</td>

<td>
  
```kotlin
InstagramLikeButton(
    modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
    isLiked = isLiked,
    onClick = {
        isLiked = !isLiked
    }
)
```

</td>

<td>
  
```kotlin
InstagramDotIndicator(
    modifier = Modifier
        .fillMaxWidth()
        .height(16.dp),
    currentPage = currentPage,
    totalPage = pageCount,
    spacePadding = 8.dp
)
```

</td>

  </tr>
  
</table>

# Youtube

<table>
  <th>shimmering</th>
  
  <tr>
    <td align="center"><img src = "https://github.com/user-attachments/assets/3d35b704-d387-41b2-aa47-e17d447b314b" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
ShimmeringPlaceholder(
    modifier = Modifier
        .fillMaxWidth()
        .height(20.dp),
    backgroundColor = backgroundColor
    // !! it's the background color of
    // placeholder's external composable
)
```
</td>


  </tr>
  
</table>

# X (Twitter)

<table>
  <th>heart</th>
  
  <tr>
    <td align="center"><img src = "https://github.com/user-attachments/assets/af59a028-b7cc-4e24-88a5-7bd62b5e557d" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
TwitterLikeButton(
    modifier = Modifier.size(50.dp),
    isLiked = isLiked
) {
    isLiked = !isLiked
}
```
</td>

  </tr>
  
</table>
