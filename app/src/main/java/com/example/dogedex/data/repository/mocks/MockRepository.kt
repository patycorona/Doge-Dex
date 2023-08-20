package com.example.dogedex.data.repository.mocks

import com.example.dogedex.domain.model.DogModel

class MockRepository {
    companion object{
        val getDogModelMock = DogModel( 8,4, "Pekinés", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02086079-pekinese.png?alt=media&token=f3cb4225-6690-42f2-a492-b77fcdeb5ee3",
            "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false)

        val getDogModelMock2 = DogModel( 16,12, "Beagle", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02088364-beagle.png?alt=media&token=7e99c6c2-6c32-4cf1-b9a8-dc0b250f6f58",
            "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false)

        val getDogModelMock3 = DogModel( 12,8, "Toy Terrier", "Toy","25.3","28.0","https://firebasestorage.googleapis.com/v0/b/perrodex-app.appspot.com/o/dog_details_images%2Fn02087046-toy_terrier.png?alt=media&token=1615339b-1f0a-4eaa-b6bb-64619e96ccc4",
            "10 - 12","Juguetón, feliz, amistoso","4 kg", "5 kg",false)
    }

}