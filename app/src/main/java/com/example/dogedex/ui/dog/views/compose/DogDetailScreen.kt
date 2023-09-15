package com.example.dogedex.ui.dog.views.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.dogedex.R
import com.example.dogedex.domain.model.DogModel


@ExperimentalCoilApi
@Composable
fun DogDetailScreen(
    dog:DogModel,
    onButtonClicked:()-> Unit,
    onErrorDialogDismiss: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.ic_launcher_background))
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.TopCenter
    ){

        dogInformation(dog)

        AsyncImage(
            model = dog.image_url,
            contentDescription = dog.name_es,
            modifier = Modifier
                .width(270.dp)
                .padding(top = 80.dp)
        )

        FloatingActionButton(
            modifier = Modifier.
            align(alignment = Alignment.BottomCenter),
            backgroundColor = colorResource(id = R.color.colorPrimaryDark),
            onClick = { onButtonClicked() }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = colorResource(id = R.color.white)
            )
        }

        if(dog == null) errorDialog( onErrorDialogDismiss)

    }
}

@Composable
fun errorDialog(
    onErrorDialogDismiss:() -> Unit
){
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.error_dialog))
        },
        confirmButton = {
            Button(onClick = { onErrorDialogDismiss()}){
                Text(stringResource(id = R.string.try_again))
            }
        }
    )
}

@Composable
fun dogInformation(dog: DogModel) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 180.dp)
    ){
        Surface(
           modifier = Modifier
               .fillMaxWidth(),
            shape = RoundedCornerShape (4.dp),
            color = colorResource(id = android.R.color.white)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = stringResource(R.string.dog_index_format, dog.index),
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = colorResource(id = R.color.text_black),
                    fontSize = 32.sp,
                    textAlign = TextAlign.End
                )
                Text(
                    text = dog.name_es,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                    color = colorResource(id = R.color.text_black),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                LifeIcon()

                Text(
                    stringResource(id = R.string.dog_life_expectancy_format, dog.life_expectancy),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.text_black),
                )
                Text(
                    text = dog.temperament,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_black),
                    fontWeight = FontWeight.Medium
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                    color = colorResource(id = R.color.divider),
                    thickness = dimensionResource(id = R.dimen.spacy_1dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dogDataColumn(modifier = Modifier.weight(1f),stringResource(id = R.string.female),
                        dog.weight_female, dog.height_female)

                    DividerVertical()

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = dog.dog_type,
                            modifier = Modifier.padding(top = 8.dp),
                            color = colorResource(id = R.color.text_black ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.group),
                            modifier = Modifier.padding(top = 8.dp),
                            color = colorResource(id = R.color.dark_gray ),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    DividerVertical()

                    dogDataColumn(modifier = Modifier.weight(1f),stringResource(id = R.string.male),
                        dog.weight_male, dog.height_male)
                }
            }
        }
    }
}

@Composable
private fun DividerVertical() {
    Divider(
        modifier = Modifier
            .height(42.dp)
            .width(1.dp),
        color = colorResource(id = R.color.divider)
    )
}

@Composable
private fun LifeIcon(){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 80.dp)
    ){
        Surface(
            shape = CircleShape,
            color = colorResource(id = R.color.color_primary)
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_hearth_white),
                contentDescription = null,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .padding(4.dp),
                tint = colorResource(id = R.color.white)
            )
        }
        Surface(
            
            shape = RoundedCornerShape(bottomEnd = 2.dp, topEnd = 2.dp),
            modifier = Modifier
                .width(200.dp)
                .height(6.dp),
            color = colorResource(id = R.color.color_primary)
        ){}
    }
}

@Composable
private fun dogDataColumn(
    modifier :Modifier = Modifier,
    genre: String, weight:String, height:String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = genre,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.text_black),
        )
        Text(
            text = weight,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.weight),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
        Text(
            text = height, // dog.height_female,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_black),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.height),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}


@Preview
@Composable
fun DetailScreenPreview(){

    val dog = DogModel( 12,8, "Toy Terrier", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02087046-toy_terrier.png?alt=media&token=1615339b-1f0a-4eaa-b6bb-64619e96ccc4",
        "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false)
    DogDetailScreen(dog, onButtonClicked = {}, onErrorDialogDismiss = {})
}

